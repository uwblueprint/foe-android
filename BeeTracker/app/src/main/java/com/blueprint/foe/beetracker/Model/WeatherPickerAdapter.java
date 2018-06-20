package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.foe.beetracker.Listeners.OnBeePartSelectedListener;
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
    private static int SIZE = 80;
    private List<WeatherOption> mWeatherOptions;

    @Override
    public int getItemCount() {
        return mWeatherOptions.size();
    }

    public WeatherPickerAdapter(List<WeatherOption> weatherOptions) {
        this.mWeatherOptions = weatherOptions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.layeredImage);
            mTextView = (TextView) v.findViewById(R.id.weatherType);
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
        final WeatherOption weatherOption = mWeatherOptions.get(position);

        Drawable[] layers;
        if (weatherOption.isSelected()) {
            // Stack the weather option icon and a green circle to indicate that it is selected.
            layers = new Drawable[2];

            // Create a green circle to show the selected status and round the corners
            Bitmap greenBitmap = BitmapFactory.decodeResource(holder.mImageView.getContext().getResources(), R.mipmap.selection_outline);
            RoundedBitmapDrawable selectedDrawable = RoundedBitmapDrawableFactory.create(holder.mImageView.getContext().getResources(), greenBitmap);
            selectedDrawable.setCircular(true);
            selectedDrawable.setAntiAlias(true);
            layers[0] =  selectedDrawable;

            layers[1] = weatherOption.getDrawable(holder.mImageView.getContext());
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            int inset = 20;
            layerDrawable.setLayerInset(1, inset, inset, inset, inset);
            holder.mImageView.setImageDrawable(layerDrawable);
        } else {
            holder.mImageView.setImageDrawable(weatherOption.getDrawable(holder.mImageView.getContext()));
        }

        holder.mTextView.setText(weatherOption.getWeather().toString());

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update submission with the user's choice
                SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
                Submission submission = activity.getSubmission();
                submission.setWeather(weatherOption.getWeather());

                unselectAllItems();
                weatherOption.setSelection(true);
                notifyDataSetChanged();
            }
        });
    }

    public void unselectAllItems() {
        for (int i = 0; i < mWeatherOptions.size(); i++) {
            mWeatherOptions.get(i).setSelection(false);
        }
    }
}