package com.blueprint.foe.beetracker.Model;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.foe.beetracker.R;
import com.blueprint.foe.beetracker.SubmissionInterface;

import java.util.List;

/**
 * A RecyclerView Adapter to hold the weather option images. Used by ReviewFragment.
 * Looks like a horizontal ListView with round weather option images. The images optionally have a
 * green circle around them to indicate they are selected.
 */
public class WeatherPickerAdapter extends RecyclerView.Adapter<WeatherPickerAdapter.ViewHolder> {
    private static String TAG = WeatherPickerAdapter.class.toString();
    private List<Submission.Weather> mWeatherTypes;

    @Override
    public int getItemCount() {
        return mWeatherTypes.size();
    }

    public WeatherPickerAdapter(List<Submission.Weather> weatherOptions) {
        this.mWeatherTypes = weatherOptions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public ImageView mSelection;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.layeredImage);
            mTextView = (TextView) v.findViewById(R.id.weatherType);
            mSelection = (ImageView) v.findViewById(R.id.selection);
        }
    }

    @Override
    public WeatherPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_option, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Submission.Weather weather = mWeatherTypes.get(position);

        Drawable[] layers;
        holder.mImageView.setImageResource(weather.getResource());

        SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
        Submission submission = activity.getSubmission();
        if (weather.equals(submission.getWeather())) {
            holder.mSelection.setVisibility(View.VISIBLE);
        } else {
            holder.mSelection.setVisibility(View.GONE);
        }

        holder.mTextView.setText(weather.toString());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update submission with the user's choice
                SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
                Submission submission = activity.getSubmission();
                submission.setWeather(weather);

                notifyDataSetChanged();
            }
        });
    }
}