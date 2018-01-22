package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueprint.foe.beetracker.OnBeePartSelectedListener;
import com.blueprint.foe.beetracker.R;
import com.blueprint.foe.beetracker.SubmissionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * An ArrayAdapter for the fact collection. Used by LearnActivity.
 */
public class PartsPickerAdapter extends RecyclerView.Adapter<PartsPickerAdapter.ViewHolder> {
    private static String TAG = PartsPickerAdapter.class.toString();
    private static int SIZE = 80;
    private BeePartType type;
    private List<BeePart> beeParts;
    private OnBeePartSelectedListener mListener;

    public enum BeePartType {
        Face, Abdomen, Thorax
    }

    @Override
    public int getItemCount() {
        return beeParts.size();
    }

    public PartsPickerAdapter(List<BeePart> beeParts, BeePartType type, OnBeePartSelectedListener listener) {
        this.type = type;
        this.beeParts = beeParts;
        this.mListener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
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

    // Create new views (invoked by the layout manager)
    @Override
    public PartsPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view to hold our layered image
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bee_part, parent, false);

        // Create a grey background and round its corners
        Bitmap backgroundBitmap = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888);
        backgroundBitmap.eraseColor(parent.getContext().getResources().getColor(R.color.medium_grey));
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
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        // Round the corners of the bee part image
        final BeePart beePart = beeParts.get(position);

        Drawable[] layers;
        if (beePart.isSelected()) {
            // Create a green circle to show the selected status and round the corners
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
            // Stack the two drawables on top of each other
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
                SubmissionActivity activity = (SubmissionActivity) holder.mImageView.getContext();
                Submission submission = activity.getSubmission();
                switch(type) {
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

                for (int i = 0; i < beeParts.size(); i++) {
                    beeParts.get(i).setSelection(false);
                }
                beePart.setSelection(!beePart.isSelected()); // toggle
                mListener.onBeePartSelected();
                notifyDataSetChanged();
            }
        });
    }
}