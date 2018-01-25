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

import com.blueprint.foe.beetracker.OnBeePartSelectedListener;
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
    private BeePart.BeePartType mType;
    private List<BeePart> mBeeParts;
    private OnBeePartSelectedListener mListener;

    @Override
    public int getItemCount() {
        return mBeeParts.size();
    }

    public PartsPickerAdapter(List<BeePart> beeParts, BeePart.BeePartType type, OnBeePartSelectedListener listener) {
        this.mType = type;
        this.mBeeParts = beeParts;
        this.mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public RoundedBitmapDrawable mGreyBackground;
        public RoundedBitmapDrawable mSelected;
        public RoundedBitmapDrawable mBeeImage;
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
        Bitmap greenBitmap = BitmapFactory.decodeResource(parent.getContext().getResources(), R.drawable.selection_outline);
        RoundedBitmapDrawable selectedDrawable = RoundedBitmapDrawableFactory.create(parent.getContext().getResources(), greenBitmap);
        selectedDrawable.setCircular(true);
        selectedDrawable.setAntiAlias(true);
        ViewHolder vh = new ViewHolder(v, selectedDrawable, roundedBackgroundDrawable);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BeePart beePart = mBeeParts.get(position);

        Drawable[] layers;
        if (beePart.isSelected()) {
            // Stack the bee part transparent icon with a grey circular background and a green
            // circle to indicate that it is selected.
            layers = new Drawable[3];
            layers[0] =  holder.mSelected;
            layers[1] = holder.mGreyBackground;
            layers[2] = beePart.getDrawable();
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            int inset = 30;
            layerDrawable.setLayerInset(1, inset, inset, inset, inset);
            layerDrawable.setLayerInset(2, inset, inset, inset, inset);
            holder.mImageView.setImageDrawable(layerDrawable);
        } else {
            // Stack the two drawables (bee part and grey background) on top of each other
            layers = new Drawable[2];
            layers[0] =  holder.mGreyBackground;
            layers[1] = beePart.getDrawable();
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            holder.mImageView.setImageDrawable(layerDrawable);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update submission with the user's choice
                SubmissionInterface activity = (SubmissionInterface) holder.mImageView.getContext();
                Submission submission = activity.getSubmission();
                switch(mType) {
                    case Face:
                        submission.setFace(beePart.getIndex());
                        break;
                    case Thorax:
                        submission.setThorax(beePart.getIndex());
                        break;
                    default:
                        submission.setAbdomen(beePart.getIndex());
                        break;
                }

                for (int i = 0; i < mBeeParts.size(); i++) {
                    mBeeParts.get(i).setSelection(false);
                }
                beePart.setSelection(!beePart.isSelected()); // toggle
                mListener.onBeePartSelected();
                notifyDataSetChanged();
            }
        });
    }
}