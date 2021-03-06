package com.blueprint.foe.beetracker.Model;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.R;
import com.blueprint.foe.beetracker.SightingFragment;
import com.blueprint.foe.beetracker.SignUpFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An ArrayAdapter for the submission collection. Used by HistoryActivity.
 */
public class SubmissionsAdapter extends ArrayAdapter<CompletedSubmission> {
    private static String TAG = SubmissionsAdapter.class.toString();

    public SubmissionsAdapter(Context context, List<CompletedSubmission> submissions) {
        super(context, 0, submissions);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.submission_view, parent, false);
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(getContext(), R.color.gradient1),
                        ContextCompat.getColor(getContext(), R.color.gradient2),
                        ContextCompat.getColor(getContext(), R.color.gradient3),
                        ContextCompat.getColor(getContext(), R.color.gradient4),
                        ContextCompat.getColor(getContext(), R.color.gradient5),
                        ContextCompat.getColor(getContext(), R.color.gradient6),
                        ContextCompat.getColor(getContext(), R.color.gradient7),
                        ContextCompat.getColor(getContext(), R.color.gradient8)

                });

        final CompletedSubmission submission = getItem(position);

        LinearLayout container = (LinearLayout) convertView.findViewById(R.id.submissionViewContainer);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String submissionString = gson.toJson(submission);
                bundle.putString(SightingFragment.SIGHTING_KEY, submissionString);
                FragmentTransaction fragmentTransaction = ((Activity) getContext()).getFragmentManager().beginTransaction();
                SightingFragment sightingFragment = new SightingFragment();
                sightingFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.history_container_layout, sightingFragment);
                fragmentTransaction.addToBackStack("main");
                fragmentTransaction.commit();
            }
        });

        ImageView gradient = (ImageView) convertView.findViewById(R.id.imgGradient);
        gradient.setImageDrawable(gradientDrawable);

        TextView submissionDate = (TextView) convertView.findViewById(R.id.tvDate);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
        String formatted = formatter.format(submission.getDate());
        submissionDate.setText(formatted);
        TextView submissionSpecies = (TextView) convertView.findViewById(R.id.tvSpecies);
        if (submission.getSpecies() != null) {
            submissionSpecies.setText(submission.getSpecies().getEnglishName());
        } else {
            submissionSpecies.setText(getContext().getString(R.string.unidentified_species));
        }
        TextView submissionAddress = (TextView) convertView.findViewById(R.id.tvLocation);
        submissionAddress.setText(submission.getStreetAddress());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgSubmission);
        Picasso.with(getContext())
                .load(submission.getImageUrl())
                .placeholder(R.mipmap.submission_placeholder)
                .into(imageView);
        return convertView;
    }
}
