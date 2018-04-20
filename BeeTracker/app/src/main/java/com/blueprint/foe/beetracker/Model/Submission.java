package com.blueprint.foe.beetracker.Model;

import com.google.android.gms.location.places.Place;

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
        return mHabitat != null && mHabitat != Habitat.unselected && mWeather != null && mWeather != Weather.unselected && mPlace != null;
    }

    public enum Species {
        impatiens, ternarius, rufocinctus, bimaculatus, borealis, vagans, affinis,
        griseocollis, citrinus, perplexus, pensylvanicus, sylvicola, sandersoni,
        nevadensis, auricomus, terricola, fervidus, flavifrons, occidentalis, melanopygus,
        bifarius, huntii, vosnesenski, cryptarum, mixtus, centralis
    } // Missing bohemicus

    public enum Habitat {
        unselected, back_yard, community_garden, city_park, rural, golf_course, roadside, woodland, farmland, school_grounds, other;

        @Override
        public String toString() {
            switch (this) {
                case unselected:
                    return "";
                case back_yard:
                    return "Back Yard";
                case community_garden:
                    return "Community Garden";
                case city_park:
                    return "City Park";
                case rural:
                    return "Rural";
                case golf_course:
                    return "Golf Course";
                case roadside:
                    return "Roadside";
                case woodland:
                    return "Woodland";
                case farmland:
                    return "Farmland";
                case school_grounds:
                    return "School Grounds";
                case other:
                    return "other";
                default:
                    return "There was an error.";
            }
        }
    }

    public enum Weather {
        unselected, sunny, partly_cloudy, cloudy, rain, windy, other;

        @Override
        public String toString() {
            switch (this) {
                case unselected:
                    return "";
                case sunny:
                    return "Sunny";
                case partly_cloudy:
                    return "Partly Cloudy";
                case cloudy:
                    return "Cloudy";
                case rain:
                    return "Rain";
                case windy:
                    return "Windy";
                case other:
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
