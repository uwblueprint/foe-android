package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);

        final ReviewFragment fragment = this;
        TextView submitButton = (TextView) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        Bitmap bitmap = BitmapFactory.decodeFile(submission.getImageFilePath());
        int width = bitmap.getWidth();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, container.getWidth(), (int)(((double)bitmap.getHeight() / (double)width) * container.getWidth()), false);
        ImageView preview = (ImageView) view.findViewById(R.id.beeImageView);
        preview.setImageBitmap(scaled);

        // Set up interactive UI elements (spinner, location picker)
        // TODO (https://github.com/uwblueprint/foe/issues/32) : Set up an adapter that extends partsPickerAdapter
        final Spinner weatherSpinner = (Spinner) view.findViewById(R.id.weather_spinner);
        final Submission.Weather[] weathers = Submission.Weather.values();
        ArrayAdapter<Submission.Weather> weatherAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, weathers);
        weatherAdapter.setDropDownViewResource(R.layout.spinner_item);
        weatherSpinner.setAdapter(weatherAdapter);
        weatherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int item = (int) weatherSpinner.getSelectedItemId();
                submission.setWeather(weathers[item]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(TAG, "nothing selected");
            }
        });

        final Spinner habitatSpinner = (Spinner) view.findViewById(R.id.habitat_spinner);
        final Submission.Habitat[] habitats = Submission.Habitat.values();
        ArrayAdapter<Submission.Habitat> habitatAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_item, habitats);
        habitatAdapter.setDropDownViewResource(R.layout.spinner_item);
        habitatSpinner.setAdapter(habitatAdapter);
        habitatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int item = (int) habitatSpinner.getSelectedItemId();
                submission.setHabitat(habitats[item]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ImageView searchIcon = (ImageView)((LinearLayout)autocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.location_search_icon));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                submission.setLocation(place);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), "There was an error finding your place.", Toast.LENGTH_LONG);
                Log.e(TAG, "An error occurred: " + status);
            }
        });

        return view;
    }

    @Override
    public void onDialogFinishClick() {
        // User touched the dialog's finish button
        getActivity().finish();
    }
}
