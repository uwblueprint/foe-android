package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.API.TokenHelper;
import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.blueprint.foe.beetracker.Model.CompletedSubmission;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;
import com.blueprint.foe.beetracker.Model.Submission;
import com.blueprint.foe.beetracker.Model.WeatherOption;
import com.blueprint.foe.beetracker.Model.WeatherPickerAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static com.blueprint.foe.beetracker.Model.Submission.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment will allow the user to review the species, location, as well as add the
 * environment type and current weather.
 */
public class ReviewFragment extends Fragment implements BeeAlertDialogListener {
    private static final String TAG = ReviewFragment.class.toString();
    private Spinner mHabitatSpinner;
    private CardView mCardView;
    private TextView mErrorMessage;
    private Callback submitCallback;
    private SpinningIconDialog spinningIconDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);

        final ReviewFragment fragment = this;
        TextView submitButton = (TextView) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check all fields are completed
                fragment.launchSpinnerPopup();
                SubmissionInterface submissionInterface = (SubmissionInterface) getActivity();
                CurrentSubmission submission = submissionInterface.getSubmission();
                if (!submission.isComplete()) {
                    setErrorFields(submission);
                    removeSpinnerPopup();
                    return;
                }
                submitToServer();
            }
        });

        TextView backButton = (TextView) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        // Set up image preview in top left corner
        final CurrentSubmission submission = ((SubmissionActivity) getActivity()).getSubmission();
        Bitmap bitmap = submission.getBitmap();
        int width = bitmap.getWidth();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, container.getWidth(), (int)(((double)bitmap.getHeight() / (double)width) * container.getWidth()), false);
        ImageView preview = (ImageView) view.findViewById(R.id.beeImageView);
        preview.setImageBitmap(scaled);

        // Set up interactive UI elements (spinner, location picker)
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(createAdapter(submission));

        mHabitatSpinner = (Spinner) view.findViewById(R.id.habitat_spinner);
        final Habitat[] habitats = Habitat.values();
        ArrayAdapter<Habitat> habitatAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, habitats);
        habitatAdapter.setDropDownViewResource(R.layout.spinner_item);
        mHabitatSpinner.setAdapter(habitatAdapter);
        mHabitatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int item = (int) mHabitatSpinner.getSelectedItemId();
                submission.setHabitat(habitats[item]);
                resetErrorFields(submission);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        if (submission.getHabitat() != null) {
            mHabitatSpinner.setSelection(submission.getHabitat().ordinal());
        }

        mCardView = (CardView) view.findViewById(R.id.card_view);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ImageView searchIcon = (ImageView)((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.icon_location));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                submission.setLocation(place.getName().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                resetErrorFields(submission);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), "There was an error finding your place.", Toast.LENGTH_LONG);
                Log.e(TAG, "An error occurred: " + status);
            }
        });
        if (submission.getStreetAddress() != null) {
            autocompleteFragment.setText(submission.getStreetAddress());
        }

        mErrorMessage = (TextView) view.findViewById(R.id.review_error_message);
        submitCallback = new Callback<BeeTrackerCaller.SubmissionResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.SubmissionResponse> call, Response<BeeTrackerCaller.SubmissionResponse> response) {
                removeSpinnerPopup();
                if (response.code() == 401) {
                    Log.e(TAG, "The response from the server is " + response.code() + " " + response.message());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Sorry, you've been logged out.", Toast.LENGTH_LONG).show();
                    return;
                }
                TokenHelper.setSharedPreferencesFromHeader(getActivity(), response.headers());
                if (response.code() == 422 || response.body() == null) {
                    Log.e(TAG, "The response from the server is " + response.code() + " " + response.message());
                    showErrorDialog(getString(R.string.error_message_submit));
                    return;
                }
                Log.d(TAG, "The response body: " + response.body());
                fragment.launchPopup();
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SubmissionResponse> call, Throwable t) {
                Log.e(TAG, "There was an error with the submitCallback + " + t.toString());
                t.printStackTrace();
                removeSpinnerPopup();
                showErrorDialog(getString(R.string.error_message_submit));
            }
        };

        if (submission.getSpecies() != null) {
            TextView latinSpecies = (TextView) view.findViewById(R.id.latinName);
            latinSpecies.setText("Bombus " + submission.getSpecies().toString());

            TextView englishSpecies = (TextView) view.findViewById(R.id.englishName);
            englishSpecies.setText(submission.getSpecies().getEnglishName());
        }

        return view;
    }

    public void launchPopup() {
        BeeAlertDialog dialog = new BeeAlertDialog();
        dialog.setTargetFragment(this, 1);
        Bundle args = new Bundle();
        args.putInt(BeeAlertDialog.IMAGE_SRC, R.mipmap.bee_image_popup);
        args.putString(BeeAlertDialog.HEADING, getString(R.string.submit_dialog_heading));
        args.putString(BeeAlertDialog.PARAGRAPH, getString(R.string.submit_dialog_paragraph));
        dialog.setArguments(args);
        dialog.setCancelable(false);
        dialog.show(getActivity().getFragmentManager(), "SubmissionPopup");
    }

    private void launchSpinnerPopup() {
        spinningIconDialog = new SpinningIconDialog();
        spinningIconDialog.show(getActivity().getFragmentManager(), "SpinningPopup");
    }

    private void removeSpinnerPopup() {
        if (spinningIconDialog != null) {
            spinningIconDialog.dismiss();
        }
    }

    private void submitToServer() {
        BeeTrackerCaller caller = new BeeTrackerCaller();
        try {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String accessToken = sharedPref.getString(getString(R.string.preference_login_token), null);
            String tokenType = sharedPref.getString(getString(R.string.preference_login_token_type), null);
            String client = sharedPref.getString(getString(R.string.preference_login_client), null);
            String uid = sharedPref.getString(getString(R.string.preference_login_uid), null);
            CurrentSubmission submission = ((SubmissionActivity) getActivity()).getSubmission();
            Call<BeeTrackerCaller.SubmissionResponse> token = caller.submit(submission, accessToken, tokenType, client, uid);
            token.enqueue(submitCallback);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            showErrorDialog(getString(R.string.error_message_login));
        }
    }



    // Method to set textfields to red as appropriate or reset them
    private void setErrorFields(CurrentSubmission submission) {
        if (!submission.isComplete()) {
            mErrorMessage.setVisibility(View.VISIBLE);
        }
        if (submission.getHabitat() == null || submission.getHabitat() == Habitat.Default) {
            mHabitatSpinner.setBackgroundResource(R.drawable.spinner_background_error);
        }
        if (submission.getWeather() == null) {
            mRecyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.errorRed));
        }
        if (submission.getStreetAddress() == null) {
            mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.errorRed));
        }
    }

    private void resetErrorFields(CurrentSubmission submission) {
        if (submission.isComplete()) {
            mErrorMessage.setVisibility(View.GONE);
        }
        if (submission.getHabitat() != null && submission.getHabitat() != Habitat.Default) {
            mHabitatSpinner.setBackgroundResource(R.drawable.spinner_background);
        }
        if (submission.getWeather() != null) {
            mRecyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
        if (submission.getStreetAddress() != null) {
            mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }

    private void showErrorDialog(String message) {
        BeeAlertErrorDialog dialog = new BeeAlertErrorDialog();
        dialog.setTargetFragment(this, 1);
        Bundle args = new Bundle();
        args.putString(BeeAlertErrorDialog.ERROR_MESSAGE_KEY, message);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "ErrorMessage");
    }

    @Override
    public void onDialogFinishClick(int id) {
        if (id == ERROR_DIALOG) {
            // Do nothing
        } else if (id == NORMAL_DIALOG) {
            getActivity().finish();
        }
    }

    private WeatherPickerAdapter createAdapter(Submission submission) {
        int[] weatherAssets = {R.mipmap.weather_sunny, R.mipmap.weather_partly_cloudy, R.mipmap.weather_cloudy, R.mipmap.weather_rainy};
        List<Submission.Weather> weatherOptionEnums = Arrays.asList(Submission.Weather.values());

        List<WeatherOption> weatherOptions = new ArrayList<>();
        for (int i = 0; i < weatherAssets.length; i++) {
            weatherOptions.add(new WeatherOption(weatherOptionEnums.get(i), weatherAssets[i]));
        }

        if (submission.getWeather() != null) {
            weatherOptions.get(weatherOptionEnums.indexOf(submission.getWeather())).setSelection(true);
        }

        return new WeatherPickerAdapter(weatherOptions);
    }
}
