package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * An ArrayAdapter for the submission collection. Used by HistoryActivity.
 */
public class SubmissionsAdapter extends ArrayAdapter<BeeTrackerCaller.SubmissionResponse> {
    private static String TAG = SubmissionsAdapter.class.toString();

    private BeeTrackerCaller.SubmissionResponse[] mSubmissions;
    public SubmissionsAdapter(Context context, BeeTrackerCaller.SubmissionResponse[] submissions) {
        super(context, 0, submissions);
        this.mSubmissions = submissions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.submission_view, parent, false);
        }
        try {
            final CompletedSubmission submission = getItem(position).getSubmission();

            //template submission data (illustration, text) into view
            TextView submissionDate = (TextView) convertView.findViewById(R.id.tvDate);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH);
            String formatted = formatter.format(submission.getDate());
            submissionDate.setText(formatted);
            TextView submissionStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            TextView submissionSpecies = (TextView) convertView.findViewById(R.id.tvSpecies);
            submissionSpecies.setText(submission.getSpecies().getEnglishName());
            TextView submissionAddress = (TextView) convertView.findViewById(R.id.tvLocation);
            submissionAddress.setText(submission.getStreetAddress());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imgSubmission);
            Picasso.get()
                    .load(submission.getImageUrl())
                    .placeholder(R.mipmap.submission_placeholder)
                    .into(imageView);
        } catch(ParseException e) {

        }
        return convertView;
    }
}
