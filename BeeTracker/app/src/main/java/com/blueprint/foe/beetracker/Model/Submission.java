package com.blueprint.foe.beetracker.Model;

import java.util.Date;

/**
 * Abstract class for common methods between a submission being built up (CurrentSubmission) and
 * the submissions returned from the server (CompletedSubmission)
 */
public abstract class Submission {
    // TODO: accept user input for Date https://github.com/uwblueprint/foe/issues/75
    private Date mDate = null;
    private BeeSpeciesType mSpeciesType = null;
    private Species mSpecies = null;
    private Habitat mHabitat = null;
    private Weather mWeather = null;
    private double mLatitude = 0;
    private double mLongitude = 0;
    private String mStreetAddress = null;

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
                && this.mLatitude == that.mLatitude
                && this.mLongitude == that.mLongitude
                && ((this.mStreetAddress == null && that.mStreetAddress == null)
                    || this.mStreetAddress != null && that.mStreetAddress != null && this.mStreetAddress.equals(that.mStreetAddress));
    }

    public enum Species {
        impatiens, ternarius, rufocinctus, bimaculatus, borealis, vagans, affinis,
        griseocollis, citrinus, perplexus, pensylvanicus, sylvicola, sandersoni,
        nevadensis, auricomus, terricola, fervidus, flavifrons, occidentalis, melanopygus,
        bifarius, huntii, vosnesenski, cryptarum, mixtus, centralis;

        public String getEnglishName() {
            String[] names = {
                    "Common eastern bumble bee", "Tri-coloured bumble bee",  "Red-belted bumble bee",
                    "Two-spotted bumble bee", "Northern amber bumble bee", "Half-black bumble bee",
                    "Rusty-patched bumble bee", "Brown-belted bumble bee", "Lemon cuckoo bumble bee",
                    "Confusing bumble bee", "American bumble bee", "Forest bumble bee",
                    "Sanderson bumble bee", "Nevada bumble bee", "Black and gold bumble bee",
                    "Yellow-banded bumble bee", "Yellow bumble bee", "Yellow head bumble bee",
                    "Common western bumble bee", "Black tail bumble bee", "Two-form bumble bee",
                    "Hunt bumble bee", "Vosnensky bumble bee", "Cryptic bumble bee",
                    "Fuzzy-horned bumble bee", "Central bumble bee",
            }; // Missing "Gypso cuckoo bumble bee"
            return names[this.ordinal()];
        }
    } // Missing bohemicus


    public enum Habitat {
        Default, Back_Yard, Balcony_Container_Garden, Community_Garden, City_Park, Rural, Golf_Course, Roadside, Woodland, Farmland, School_Grounds, Other;

        @Override
        public String toString() {
            switch (this) {
                case Default:
                    return "";
                case Back_Yard:
                    return "Back Yard";
                case Balcony_Container_Garden:
                    return "Balcony/Container Garden";
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
        Sunny, Partly_Cloudy, Cloudy, Rain;

        @Override
        public String toString() {
            switch (this) {
                case Sunny:
                    return "Sunny";
                case Partly_Cloudy:
                    return "Partly Cloudy";
                case Cloudy:
                    return "Cloudy";
                case Rain:
                    return "Rainy";
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
        mStreetAddress = null;
        mLatitude = 0;
        mLongitude = 0;
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

    public void setLocation(String address, double latitude, double longitude) {
        this.mStreetAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getStreetAddress() {
        return mStreetAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
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
        return mHabitat != null && mHabitat != Habitat.Default && mWeather != null && mStreetAddress != null && mLongitude != 0 && mLatitude != 0;
    }

    public enum BeeSpeciesType {
        Eastern, Western
    }
}
