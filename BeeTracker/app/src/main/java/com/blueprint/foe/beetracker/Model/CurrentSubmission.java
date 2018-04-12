package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Date;

/**
 * Represents the data object that contains all information related to submission.
 */
public class CurrentSubmission extends Submission {
    private static final String TAG = CurrentSubmission.class.toString();
    protected transient Bitmap mBitmap = null;
    protected String mImageFilePath = null;

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof CurrentSubmission)) {
            return false;
        }
        if (other == this) return true;

        CurrentSubmission that = (CurrentSubmission) other;

        return super.equals(that)
                && ((this.mImageFilePath == null && that.mImageFilePath == null)
                    || this.mImageFilePath != null && this.mImageFilePath.equals(that.mImageFilePath))
                && ((this.mBitmap == null && that.mBitmap == null)
                    || this.mBitmap != null && that.mBitmap != null && this.mBitmap.equals(that.mBitmap));
//        return this.mSpecies == that.mSpecies
//                && this.mSpeciesType == that.mSpeciesType
//                && this.mHabitat == that.mHabitat
//                && this.mWeather == that.mWeather
//                && this.mImageFilePath == that.mImageFilePath
//                && ((this.mDate == null && that.mDate == null)
//                    || this.mDate != null && that.mDate != null && this.mDate.equals(that.mDate))
//                && ((this.mBitmap == null && that.mBitmap == null)
//                    || this.mBitmap != null && that.mBitmap != null && this.mBitmap.equals(that.mBitmap))
//                && ((this.mPlace == null && that.mPlace == null)
//                    || this.mPlace != null && that.mPlace != null && this.mPlace.equals(that.mPlace));
    }


    @Override
    public void reset() {
        super.reset();
        mImageFilePath = null;
        mBitmap = null;
    }

    public CurrentSubmission() {
        reset();
    }

    public String getImageFilePath() {
        return mImageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        mImageFilePath = imageFilePath;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public boolean isComplete() {
        return super.isComplete() && mBitmap != null;
    }

    /* For debug only */
    public void print() {
        Log.d(TAG, "Species: " + mSpecies + ", Habitat: " + mHabitat + ", Weather: " + mWeather);
        Log.d(TAG, "Location: " + mPlace);
    }

}
