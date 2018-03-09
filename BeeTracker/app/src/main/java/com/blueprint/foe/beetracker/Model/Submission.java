package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
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

    private BeeSpeciesType mSpeciesType = null;
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

        return this.mSpecies == that.mSpecies
                && this.mSpeciesType == that.mSpeciesType
                && this.mHabitat == that.mHabitat
                && this.mWeather == that.mWeather
                && this.mImageFilePath == that.mImageFilePath
                && ((this.mBitmap == null && that.mBitmap == null)
                    || this.mBitmap != null && that.mBitmap != null && this.mBitmap.equals(that.mBitmap))
                && ((this.mPlace == null && that.mPlace == null)
                    || this.mPlace != null && that.mPlace != null && this.mPlace.equals(that.mPlace));
    }

    public enum Species {
        impatiens, ternarius, rufocinctus, bimaculatus, borealis, vagans, affinis,
        griseocollis, citrinus, perplexus, pensylvanicus, sylvicola, sandersoni,
        nevadensis, auricomus, terricola, fervidus, flavifrons, occidentalis, melanopygus,
        bifarius1, huntii, vosnesenski, cryptarum, mixtus, centralis
    }


    public enum Habitat {
        Default, HouseGarden, Park, Swamp, PublicGarden, Lake, Lawn;

        @Override
        public String toString() {
            switch (this) {
                case Default:
                    return "";
                case HouseGarden:
                    return "House Garden";
                case Park:
                    return "Park";
                case Swamp:
                    return "Swamp";
                case PublicGarden:
                    return "Public Garden";
                case Lake:
                    return "Lake";
                case Lawn:
                    return "Lawn";
                default:
                    return "There was an error.";
            }
        }
    }

    public enum Weather {
        Default, Sunny, PartlyCloudy, Cloudy, Raining, Hailing;

        @Override
        public String toString() {
            switch (this) {
                case Default:
                    return "";
                case Sunny:
                    return "Sunny";
                case PartlyCloudy:
                    return "Partly Cloudy";
                case Cloudy:
                    return "Cloudy";
                case Raining:
                    return "Raining";
                case Hailing:
                    return "Hailing";
                default:
                    return "There was an error.";
            }
        }
    }

    public void reset() {
        mSpeciesType = null;
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

    public BeeSpeciesType getSpeciesType() {
        return mSpeciesType;
    }

    public Species getSpecies() {
        return mSpecies;
    }

    public void setSpecies(Species species, BeeSpeciesType isEastern) {
        mSpeciesType = isEastern;
        mSpecies = species;
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
        Log.d(TAG, "Species: " + mSpecies + ", Habitat: " + mHabitat + ", Weather: " + mWeather);
        Log.d(TAG, "Location: " + mPlace);
    }

    public enum BeeSpeciesType {
        Eastern, Western
    }
}
