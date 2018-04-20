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

import com.blueprint.foe.beetracker.R;

import java.util.ArrayList;

/**
 * An ArrayAdapter for the submission collection. Used by HistoryActivity.
 */
public class SubmissionsAdapter extends ArrayAdapter<CompletedSubmission> {
    private static String TAG = SubmissionsAdapter.class.toString();

    private ArrayList<CompletedSubmission> mSubmissions;
    public SubmissionsAdapter(Context context, ArrayList<CompletedSubmission> submissions) {
        super(context, 0, submissions);
        this.mSubmissions = submissions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final CompletedSubmission submission = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.submission_view, parent, false);
        }

        //template submission data (illustration, text) into view
        TextView submissionDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView submissionStatus = (TextView) convertView.findViewById(R.id.tvStatus);
        TextView submissionSpecies = (TextView) convertView.findViewById(R.id.tvSpecies);

        return convertView;
    }
}
