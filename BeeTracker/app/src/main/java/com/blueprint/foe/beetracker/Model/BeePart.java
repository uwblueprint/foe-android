package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * This class contains information about the bee part including whether or not it is selected,
 * its index and the drawable image to be loaded into the UI component.
 */

public class BeePart {
    private int mPartIndex;
    private RoundedBitmapDrawable mDrawable;
    private boolean mSelected;

    public enum BeePartType {
        Face, Abdomen, Thorax
    }

    public BeePart(int partIndex, int drawable, Context context) {
        this.mPartIndex = partIndex;
        this.mSelected = false;
        Bitmap beeOptionBitmap = BitmapFactory.decodeResource(context.getResources(),  drawable);
        final RoundedBitmapDrawable roundedBeeOptionDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), beeOptionBitmap);
        roundedBeeOptionDrawable.setCircular(true);
        roundedBeeOptionDrawable.setAntiAlias(true);
        mDrawable = roundedBeeOptionDrawable;
    }

    public RoundedBitmapDrawable getDrawable() {
        return mDrawable;
    }

    public int getIndex() {
        return mPartIndex;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelection(boolean selection) {
        this.mSelected = selection;
    }
}
