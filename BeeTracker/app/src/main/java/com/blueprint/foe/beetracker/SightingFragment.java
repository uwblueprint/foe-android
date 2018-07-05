package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Model.CompletedSubmission;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Shows the details of a specific sighting.
 */
public class SightingFragment extends Fragment {
    public final static String SIGHTING_KEY = "SightingKey";

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sighting_fragment, container, false);

        if (getArguments() == null) {
            // ERROR
            getFragmentManager().popBackStack();
            Toast.makeText(getActivity(), "There was an error loading your sighting.", Toast.LENGTH_LONG).show();
            return view;
        }

        String submissionString = getArguments().getString(SIGHTING_KEY);
        Gson gson = new Gson();
        CompletedSubmission submission = gson.fromJson(submissionString, CompletedSubmission.class);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(getActivity(), R.color.gradient1),
                        ContextCompat.getColor(getActivity(), R.color.gradient2),
                        ContextCompat.getColor(getActivity(), R.color.gradient3),
                        ContextCompat.getColor(getActivity(), R.color.gradient4),
                        ContextCompat.getColor(getActivity(), R.color.gradient5),
                        ContextCompat.getColor(getActivity(), R.color.gradient6),
                        ContextCompat.getColor(getActivity(), R.color.gradient7),
                        ContextCompat.getColor(getActivity(), R.color.gradient8)

                });

        ImageView gradient = (ImageView) view.findViewById(R.id.imgGradient);
        gradient.setImageDrawable(gradientDrawable);

        ImageView capturedImage = (ImageView) view.findViewById(R.id.ivBee);
        Picasso.with(getActivity())
                .load(submission.getImageUrl())
                .placeholder(R.mipmap.submission_placeholder)
                .into(capturedImage);

        TextView date = (TextView) view.findViewById(R.id.tvDate);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH);
        String formatted = formatter.format(submission.getDate());
        date.setText(formatted);

        TextView englishSpecies = (TextView) view.findViewById(R.id.species_english);
        ImageView beeSpecies = (ImageView) view.findViewById(R.id.ivBeeSpecies);
        TextView latinSpecies = (TextView) view.findViewById(R.id.species_latin);
        if (submission.getSpecies() != null) {
            englishSpecies.setText(submission.getSpecies().getEnglishName());
            latinSpecies.setText(getString(R.string.latin_species, submission.getSpecies().toString()));
            beeSpecies.setImageResource(submission.getSpecies().getResource());
        }

        TextView location = (TextView) view.findViewById(R.id.location);
        location.setText(submission.getStreetAddress());

        ImageView weatherIcon = (ImageView) view.findViewById(R.id.weather_icon);
        weatherIcon.setImageResource(submission.getWeather().getResource());

        TextView weatherText = (TextView) view.findViewById(R.id.weather_status);
        weatherText.setText(submission.getWeather().toString());

        TextView habitatText = (TextView) view.findViewById(R.id.habitat_status);
        habitatText.setText(submission.getHabitat().toString());

        CardView closeButton = (CardView) view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
