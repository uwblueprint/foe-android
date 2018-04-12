package com.blueprint.foe.beetracker.Model;

import com.google.android.gms.location.places.Place;

import java.io.Console;
import java.util.Date;

/**
 * Created by luisa on 2018-04-09.
 */

public abstract class Submission {
    // TODO: accept user input for Date
    protected Date mDate = null;
    protected BeeSpeciesType mSpeciesType = null;
    protected Species mSpecies = null;
    protected Habitat mHabitat = null;
    protected Weather mWeather = null;
    protected Place mPlace = null;

    //@Override
    //public abstract boolean equals(Object other);

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
                && ((this.mDate == null && that.mDate == null)
                || this.mDate != null && that.mDate != null && this.mDate.equals(that.mDate))
                && ((this.mPlace == null && that.mPlace == null)
                || this.mPlace != null && that.mPlace != null && this.mPlace.equals(that.mPlace));
    }

    public void reset() {
        mSpeciesType = null;
        mSpecies = null;
        mHabitat = null;
        mWeather = null;
        mPlace = null;
        mDate = null;
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

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isComplete() {
        return mHabitat != null && mHabitat != Habitat.Default && mWeather != null && mWeather != Weather.Default && mPlace != null;
    }

    public enum Species {
        impatiens, ternarius, rufocinctus, bimaculatus, borealis, vagans, affinis,
        griseocollis, citrinus, perplexus, pensylvanicus, sylvicola, sandersoni,
        nevadensis, auricomus, terricola, fervidus, flavifrons, occidentalis, melanopygus,
        bifarius, huntii, vosnesenski, cryptarum, mixtus, centralis
    } // Missing bohemicus

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
                    return "Rain";
                case Windy:
                    return "Windy";
                case Other:
                    return "Other";
                default:
                    return "There was an error.";
            }
        }
    }

    public enum BeeSpeciesType {
        Eastern, Western
    }
}
