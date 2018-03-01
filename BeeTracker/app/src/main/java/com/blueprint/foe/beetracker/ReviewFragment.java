package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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

import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.blueprint.foe.beetracker.Model.Submission;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * This fragment will allow the user to review the species, location, as well as add the
 * environment type and current weather.
 */
public class ReviewFragment extends Fragment implements BeeAlertDialogListener {
    private static final String TAG = ReviewFragment.class.toString();
    private Spinner mHabitatSpinner;
    private Spinner mWeatherSpinner;
    private CardView mCardView;
    private TextView mErrorMessage;
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);

        final ReviewFragment fragment = this;
        TextView submitButton = (TextView) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check all fields are completed
                SubmissionInterface submissionInterface = (SubmissionInterface) getActivity();
                Submission submission = submissionInterface.getSubmission();
                if (!submission.isComplete()) {
                    setErrorFields(submission);
                    return;
                }

                BeeAlertDialog dialog = new BeeAlertDialog();
                dialog.setTargetFragment(fragment, 1);
                Bundle args = new Bundle();
                args.putInt(BeeAlertDialog.IMAGE_SRC, R.drawable.bee_image_popup);
                args.putString(BeeAlertDialog.HEADING, getString(R.string.submit_dialog_heading));
                args.putString(BeeAlertDialog.PARAGRAPH, getString(R.string.submit_dialog_paragraph));
                dialog.setArguments(args);
                dialog.show(getActivity().getFragmentManager(), "SubmissionPopup");
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
        final Submission submission = ((SubmissionActivity) getActivity()).getSubmission();
        Bitmap bitmap = submission.getBitmap();
        int width = bitmap.getWidth();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, container.getWidth(), (int)(((double)bitmap.getHeight() / (double)width) * container.getWidth()), false);
        ImageView preview = (ImageView) view.findViewById(R.id.beeImageView);
        preview.setImageBitmap(scaled);

        // Set up interactive UI elements (spinner, location picker)
        // TODO (https://github.com/uwblueprint/foe/issues/32) : Set up an adapter that extends partsPickerAdapter
        mWeatherSpinner = (Spinner) view.findViewById(R.id.weather_spinner);
        final Submission.Weather[] weathers = Submission.Weather.values();
        ArrayAdapter<Submission.Weather> weatherAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, weathers);
        weatherAdapter.setDropDownViewResource(R.layout.spinner_item);
        mWeatherSpinner.setAdapter(weatherAdapter);
        mWeatherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int item = (int) mWeatherSpinner.getSelectedItemId();
                submission.setWeather(weathers[item]);
                resetErrorFields(submission);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "nothing selected");
            }
        });

        mHabitatSpinner = (Spinner) view.findViewById(R.id.habitat_spinner);
        final Submission.Habitat[] habitats = Submission.Habitat.values();
        ArrayAdapter<Submission.Habitat> habitatAdapter = new ArrayAdapter<>(getActivity(),
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

        mCardView = (CardView) view.findViewById(R.id.card_view);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ImageView searchIcon = (ImageView)((LinearLayout) autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.icon_location));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                submission.setLocation(place);
                resetErrorFields(submission);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), "There was an error finding your place.", Toast.LENGTH_LONG);
                Log.e(TAG, "An error occurred: " + status);
            }
        });

        mErrorMessage = (TextView) view.findViewById(R.id.review_error_message);

        return view;
    }

    @Override
    public void onDialogFinishClick() {
        // User touched the dialog's finish button
        getActivity().finish();
    }

    // Method to set textfields to red as appropriate or reset them
    private void setErrorFields(Submission submission) {
        if (!submission.isComplete()) {
            mErrorMessage.setVisibility(View.VISIBLE);
        }
        if (submission.getHabitat() == null || submission.getHabitat() == Submission.Habitat.Default) {
            mHabitatSpinner.setBackgroundResource(R.drawable.spinner_background_error);
        }
        if (submission.getWeather() == null || submission.getWeather() == Submission.Weather.Default) {
            mWeatherSpinner.setBackgroundResource(R.drawable.spinner_background_error);
        }
        if (submission.getLocation() == null) {
            mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.errorRed));
        }
    }

    private void resetErrorFields(Submission submission) {
        if (submission.isComplete()) {
            mErrorMessage.setVisibility(View.GONE);
        }
        if (submission.getHabitat() != null && submission.getHabitat() != Submission.Habitat.Default) {
            mHabitatSpinner.setBackgroundResource(R.drawable.spinner_background);
        }
        if (submission.getWeather() != null && submission.getWeather() != Submission.Weather.Default) {
            mWeatherSpinner.setBackgroundResource(R.drawable.spinner_background);
        }
        if (submission.getLocation() != null) {
            mCardView.setCardBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }
    }
}
