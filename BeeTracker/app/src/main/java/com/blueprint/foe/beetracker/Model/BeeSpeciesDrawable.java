package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.GridLayout;

/**
 * This class contains information about the bee part including whether or not it is selected,
 * its index and the drawable image to be loaded into the UI component.
 */
public class BeeSpeciesDrawable {
    private Submission.Species mSpecies;
    private Submission.BeeSpeciesType mSpeciesType;
    private int drawable;
    private boolean mSelected;

    public BeeSpeciesDrawable(Submission.Species species, Submission.BeeSpeciesType speciesType, int drawable, Context context) {
        this.mSpecies = species;
        this.mSpeciesType = speciesType;
        this.mSelected = false;
        this.drawable = drawable;
    }

    // Moved this method from the constructor to here because we were getting OutOfMemory error.
    // This is an OK fix, but we should investigate different ways of storing all these images in
    // memory and compressing them. TODO: https://github.com/uwblueprint/foe/issues/51
    public RoundedBitmapDrawable getDrawable(Context context) {
        Bitmap beeOptionBitmap = BitmapFactory.decodeResource(context.getResources(),  drawable);
        final RoundedBitmapDrawable roundedBeeOptionDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), beeOptionBitmap);
        roundedBeeOptionDrawable.setCircular(true);
        roundedBeeOptionDrawable.setAntiAlias(true);
        return roundedBeeOptionDrawable;
    }

    public Submission.Species getSpecies() {
        return mSpecies;
    }

    public Submission.BeeSpeciesType getSpeciesType() {
        return mSpeciesType;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelection(boolean selection) {
        this.mSelected = selection;
    }
}
