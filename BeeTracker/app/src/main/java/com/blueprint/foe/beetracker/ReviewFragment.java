package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.blueprint.foe.beetracker.Model.Submission;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * This fragment will allow the user to review the species, location, as well as add the
 * environment type and current weather.
 */

public class ReviewFragment extends Fragment {
    private static final String TAG = ReviewFragment.class.toString();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);

        TextView submitButton = (TextView) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: show popup https://github.com/uwblueprint/foe/issues/28
                getActivity().finish();
            }
        });

        TextView backButton = (TextView) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        Submission submission = ((SubmissionActivity) getActivity()).getSubmission();
        Bitmap bitmap = BitmapFactory.decodeFile(submission.getImageFilePath());
        int width = bitmap.getWidth();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, container.getWidth(), (int)(((double)bitmap.getHeight() / (double)width) * container.getWidth()), false);
        ImageView preview = (ImageView) view.findViewById(R.id.beeImageView);
        preview.setImageBitmap(scaled);

        Spinner weatherSpinner = (Spinner) view.findViewById(R.id.weather_spinner);
        ArrayAdapter<CharSequence> weatherAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weather_array, android.R.layout.simple_spinner_item);
        weatherAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weatherSpinner.setAdapter(weatherAdapter);

        Spinner habitatSpinner = (Spinner) view.findViewById(R.id.habitat_spinner);
        ArrayAdapter<CharSequence> habitatAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.habitat_array, android.R.layout.simple_spinner_item);
        habitatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habitatSpinner.setAdapter(habitatAdapter);

        // TODO: https://stackoverflow.com/questions/20422802/how-to-set-dropdown-arrow-in-spinner

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        return view;
    }
}
