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
    private OnBeePartSelectedListener mListener;

    @Override
    public int getItemCount() {
        return mBeeSpeciesDrawables.size();
    }

    public PartsPickerAdapter(List<BeeSpeciesDrawable> beeSpeciesDrawables, OnBeePartSelectedListener listener) {
        this.mBeeSpeciesDrawables = beeSpeciesDrawables;
        this.mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public RoundedBitmapDrawable mGreyBackground;
        public RoundedBitmapDrawable mSelected;
        public ViewHolder(View v, RoundedBitmapDrawable selected, RoundedBitmapDrawable greyBackground) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.layeredImage);
            mSelected = selected;
            mGreyBackground = greyBackground;
        }
    }

    @Override
    public PartsPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bee_part, parent, false);

        // Create a grey background and round its corners
        Bitmap backgroundBitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        backgroundBitmap.eraseColor(parent.getContext().getResources().getColor(R.color.mediumGrey));
        final RoundedBitmapDrawable roundedBackgroundDrawable = RoundedBitmapDrawableFactory.create(parent.getContext().getResources(), backgroundBitmap);
        roundedBackgroundDrawable.setCircular(true);
        roundedBackgroundDrawable.setAntiAlias(true);

        // Create a green circle to show the selected status and round the corners
        Bitmap greenBitmap = BitmapFactory.decodeResource(parent.getContext().getResources(), R.mipmap.selection_outline);
        RoundedBitmapDrawable selectedDrawable = RoundedBitmapDrawableFactory.create(parent.getContext().getResources(), greenBitmap);
        selectedDrawable.setCircular(true);
        selectedDrawable.setAntiAlias(true);
        ViewHolder vh = new ViewHolder(v, selectedDrawable, roundedBackgroundDrawable);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BeeSpeciesDrawable beeSpeciesDrawable = mBeeSpeciesDrawables.get(position);

        Drawable[] layers;
        if (beeSpeciesDrawable.isSelected()) {
            // Stack the bee part transparent icon with a grey circular background and a green
            // circle to indicate that it is selected.
            layers = new Drawable[3];
            layers[0] =  holder.mSelected;
            layers[1] = holder.mGreyBackground;
            layers[2] = beeSpeciesDrawable.getDrawable(holder.mImageView.getContext());
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            int inset = 90;
            layerDrawable.setLayerInset(1, inset, inset, inset, inset);
            layerDrawable.setLayerInset(2, inset, inset, inset, inset);
            holder.mImageView.setImageDrawable(layerDrawable);
        } else {
            // Stack the two drawables (bee part and grey background) on top of each other
            layers = new Drawable[2];
            layers[0] =  holder.mGreyBackground;
            layers[1] = beeSpeciesDrawable.getDrawable(holder.mImageView.getContext());
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            holder.mImageView.setImageDrawable(layerDrawable);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update submission with the user's choice
                SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
                Submission submission = activity.getSubmission();
                submission.setSpecies(beeSpeciesDrawable.getSpecies());
                submission.setSpeciesType(beeSpeciesDrawable.getSpeciesType());

                for (int i = 0; i < mBeeSpeciesDrawables.size(); i++) {
                    mBeeSpeciesDrawables.get(i).setSelection(false);
                }
                beeSpeciesDrawable.setSelection(!beeSpeciesDrawable.isSelected()); // toggle
                mListener.onBeeSpeciesSelected();
                notifyDataSetChanged();
            }
        });
    }

    public void unselectAllItems() {
        for (int i = 0; i < mBeeSpeciesDrawables.size(); i++) {
            mBeeSpeciesDrawables.get(i).setSelection(false);
        }
    }
}