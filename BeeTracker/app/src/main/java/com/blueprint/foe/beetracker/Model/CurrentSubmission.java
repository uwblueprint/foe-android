package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Date;

/**
 * Represents the data object that contains all information related to submission, as it's being
 * built up by the user. It extends Submission with a Bitmap and image file path.
 */
public class CurrentSubmission extends Submission {
    private static final String TAG = CurrentSubmission.class.toString();
    private transient Bitmap mBitmap = null;
    private String mImageFilePath = null;

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
}
