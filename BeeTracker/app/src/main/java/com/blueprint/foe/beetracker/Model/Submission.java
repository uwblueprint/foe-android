package com.blueprint.foe.beetracker.Model;

import android.location.Location;
import android.util.Log;

/**
 * Represents the data object that contains all information related to submission.
 */

public class Submission {
    private static final String TAG = Submission.class.toString();
    public static final int MAX_FACE = 3;
    public static final int MAX_THORAX = 3;
    public static final int MAX_ABDOMEN = 3;

    private int mFace; // the index of the image chosen by user
    private int mThorax; // the index of the image chosen by user
    private int mAbdomen; // the index of the image chosen by user
    private Species mSpecies = null;
    private Habitat mHabitat = null;
    private Weather mWeather = null;
    private Location mLocation = null;
    private String mImageFilePath = null;

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
                && ((this.mLocation == null && that.mLocation == null) || this.mLocation.equals(that.mLocation));
    }

    public enum Species {
        Apidae, Andrenidae, Halictidae, Megachilidae, Colletidae, Unknown
    }


    public enum Habitat {
        HouseGarden, Park, Swamp, PublicGarden, Lake, Lawn
    }

    public enum Weather {
        Sunny, PartlyCloudy, Cloudy, Raining, Hailing
    }

    public void reset() {
        mFace = -1;
        mThorax = -1;
        mAbdomen = -1;
        mSpecies = null;
        mHabitat = null;
        mWeather = null;
        mLocation = null;
        mImageFilePath = null;
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

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }

    public Species getSpecies() throws IllegalArgumentException {
        if (mSpecies != null) {
            return mSpecies;
        }

        if (mFace < 0) {
            throw new IllegalArgumentException("Face is missing");
        } else if (mThorax < 0) {
            throw new IllegalArgumentException("Thorax is missing");
        } else if (mAbdomen < 0) {
            throw new IllegalArgumentException("Abdomen is missing");
        } else if (mFace > MAX_FACE) {
            throw new IllegalArgumentException("Invalid face value");
        } else if (mThorax > MAX_THORAX) {
            throw new IllegalArgumentException("Invalid thorax value");
        } else if (mAbdomen > MAX_ABDOMEN) {
            throw new IllegalArgumentException("Invalid abdomen value");
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

    public boolean isComplete() {
        return mSpecies != null && mHabitat != null && mWeather != null && mLocation != null;
    }

    /* For debug only */
    public void print() {
        Log.d(TAG, "Face: " + mFace + ", Thorax: " + mThorax + ", Abdomen: " + mAbdomen);
        Log.d(TAG, "Species: " + mSpecies + ", Habitat: " + mHabitat + ", Weather: " + mWeather);
        Log.d(TAG, "Location: " + mLocation);
    }
}
