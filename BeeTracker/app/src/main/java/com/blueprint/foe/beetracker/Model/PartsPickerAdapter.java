package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blueprint.foe.beetracker.Listeners.OnBeePartSelectedListener;
import com.blueprint.foe.beetracker.R;
import com.blueprint.foe.beetracker.SubmissionInterface;

import java.util.List;

/**
 * A RecyclerView Adapter to hold bee part images. Used by LearnActivity.
 * Looks like a horizontal ListView with round bee part images. The images optionally have a
 * green circle around them to indicate they are selected. In order to render them according to
 * the design, a grey background is also generated to go behind the transparent bee resource.
 */
public class PartsPickerAdapter extends RecyclerView.Adapter<PartsPickerAdapter.ViewHolder> {
    private static String TAG = PartsPickerAdapter.class.toString();
    private static int SIZE = 80;
    private List<BeeSpeciesDrawable> mBeeSpeciesDrawables;

    @Override
    public int getItemCount() {
        return mBeeSpeciesDrawables.size();
    }

    public PartsPickerAdapter(List<BeeSpeciesDrawable> beeSpeciesDrawables) {
        this.mBeeSpeciesDrawables = beeSpeciesDrawables;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ImageView mSelectionView;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.layeredImage);
            mSelectionView = (ImageView) v.findViewById(R.id.selection);
        }
    }

    @Override
    public PartsPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bee_part, parent, false);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BeeSpeciesDrawable beeSpeciesDrawable = mBeeSpeciesDrawables.get(position);

        SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
        Submission submission = activity.getSubmission();
        holder.mImageView.setImageResource(beeSpeciesDrawable.getSpecies().getResource());
        if (!beeSpeciesDrawable.getSpecies().equals(submission.getSpecies())) {
            holder.mSelectionView.setVisibility(View.GONE);
        } else {
            holder.mSelectionView.setVisibility(View.VISIBLE);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update submission with the user's choice
                SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
                Submission submission = activity.getSubmission();
                submission.setSpecies(beeSpeciesDrawable.getSpecies());
                submission.setSpeciesType(beeSpeciesDrawable.getSpeciesType());

                notifyDataSetChanged();
            }
        });
    }
}