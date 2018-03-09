package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.places.Place;

/**
 * Represents the data object that contains all information related to submission.
 */

public class Submission {
    private static final String TAG = Submission.class.toString();
    public static final int MAX_FACE = 2;
    public static final int MAX_THORAX = 5;
    public static final int MAX_ABDOMEN = 8;

    private int mFace; // the index of the image chosen by user
    private int mThorax; // the index of the image chosen by user
    private int mAbdomen; // the index of the image chosen by user
    private Species mSpecies = null;
    private Habitat mHabitat = null;
    private Weather mWeather = null;
    private Place mPlace = null;
    private String mImageFilePath = null;
    private transient Bitmap mBitmap = null;

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Submission)) {
            return false;
        }
        if (other == this) return true;

        Submission that = (Submission) other;

        return this.mFace == that.mFace
                && this.mThorax == that.mThorax
                && this.mAbdomen == that.mAbdomen
                && this.mSpecies == that.mSpecies
                && this.mHabitat == that.mHabitat
                && this.mWeather == that.mWeather
                && this.mImageFilePath == that.mImageFilePath
                && ((this.mBitmap == null && that.mBitmap == null)
                    || this.mBitmap != null && that.mBitmap != null && this.mBitmap.equals(that.mBitmap))
                && ((this.mPlace == null && that.mPlace == null)
                    || this.mPlace != null && that.mPlace != null && this.mPlace.equals(that.mPlace));
    }

    public enum Species {
        Apidae, Andrenidae, Halictidae, Megachilidae, Colletidae, Unknown
    }


    public enum Habitat {
        Default, Back_Yard, Community_Garden, City_Park, Rural, Golf_Course, Roadside, Woodland, Farmland, School_Grounds, Other;

        @Override
        public String toString() {
            switch (this) {
                case Default:
                    return "";
                case Back_Yard:
                    return "Back Yard";
                case Community_Garden:
                    return "Community Garden";
                case City_Park:
                    return "City Park";
                case Rural:
                    return "Rural";
                case Golf_Course:
                    return "Golf Course";
                case Roadside:
                    return "Roadside";
                case Woodland:
                    return "Woodland";
                case Farmland:
                    return "Farmland";
                case School_Grounds:
                    return "School Grounds";
                case Other:
                    return "Other";
                default:
                    return "There was an error.";
            }
        }
    }

    public enum Weather {
        Default, Sunny, Partly_Cloudy, Cloudy, Rain, Windy, Other;

        @Override
        public String toString() {
            switch (this) {
                case Default:
                    return "";
                case Sunny:
                    return "Sunny";
                case Partly_Cloudy:
                    return "Partly Cloudy";
                case Cloudy:
                    return "Cloudy";
                case Rain:
                    return "Raining";
                case Windy:
                    return "Hailing";
                case Other:
                    return "Other";
                default:
                    return "There was an error.";
            }
        }
    }

    public void reset() {
        mFace = -1;
        mThorax = -1;
        mAbdomen = -1;
        mSpecies = null;
        mHabitat = null;
        mWeather = null;
        mPlace = null;
        mImageFilePath = null;
        mBitmap = null;
    }

    public Submission() {
        reset();
    }

    public void setFace(int f) throws IllegalArgumentException {
        if (f > MAX_FACE) {
            throw new IllegalArgumentException("Invalid face value");
        }
        mFace = f;
    }

    public int getFace() {
        return mFace;
    }

    public void setThorax(int t) throws IllegalArgumentException {
        if (t > MAX_THORAX) {
            throw new IllegalArgumentException("Invalid thorax value");
        }
        mThorax = t;
    }

    public int getThorax() {
        return mThorax;
    }

    public void setAbdomen(int a) throws IllegalArgumentException {
        if (a > MAX_ABDOMEN) {
            throw new IllegalArgumentException("Invalid abdomen value");
        }
        mAbdomen = a;
    }

    public int getAbdomen() {
        return mAbdomen;
    }

    public void setHabitat(Habitat h) {
        mHabitat = h;
    }

    public Habitat getHabitat() {
        return mHabitat;
    }

    public void setWeather(Weather w) {
        mWeather = w;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setLocation(Place place) {
        this.mPlace = place;
    }

    public Place getLocation() {
        return mPlace;
    }

    public Species getSpecies() throws IllegalArgumentException {
        if (mSpecies != null) {
            return mSpecies;
        }

        if (mFace < 0) {
            Log.e(TAG, "Face is missing");
            return Species.Unknown;
        } else if (mThorax < 0) {
            Log.e(TAG, "Thorax is missing");
            return Species.Unknown;
        } else if (mAbdomen < 0) {
            Log.e(TAG, "Abdomen is missing");
            return Species.Unknown;
        } else if (mFace > MAX_FACE) {
            Log.e(TAG, "Invalid face value");
            return Species.Unknown;
        } else if (mThorax > MAX_THORAX) {
            Log.e(TAG, "Invalid thorax value");
            return Species.Unknown;
        } else if (mAbdomen > MAX_ABDOMEN) {
            Log.e(TAG, "Invalid abdomen value");
            return Species.Unknown;
        }

        if (mFace == 0 && mThorax == 0 && mAbdomen == 0)
            mSpecies = Species.Apidae;
        else if (mFace == 0 && mThorax == 0 && mAbdomen == 1)
            mSpecies = Species.Andrenidae;
        else if (mFace == 0 && mThorax == 1 && mAbdomen == 0)
            mSpecies = Species.Halictidae;
        else if (mFace == 0 && mThorax == 1 && mAbdomen == 1)
            mSpecies = Species.Megachilidae;
        else if (mFace == 1 && mThorax == 0 && mAbdomen == 0)
            mSpecies = Species.Colletidae;
        else
            mSpecies = Species.Unknown;

        return mSpecies;
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

    public boolean isComplete() {
        return mHabitat != null && mWeather != null && mPlace != null && mImageFilePath != null;
    }

    /* For debug only */
    public void print() {
        Log.d(TAG, "Face: " + mFace + ", Thorax: " + mThorax + ", Abdomen: " + mAbdomen);
        Log.d(TAG, "Species: " + mSpecies + ", Habitat: " + mHabitat + ", Weather: " + mWeather);
        Log.d(TAG, "Location: " + mPlace);
    }
}
