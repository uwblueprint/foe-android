package com.blueprint.foe.beetracker.Model;

import com.blueprint.foe.beetracker.R;

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

        public int getResource() {
            int[] resources = {
                    R.drawable.impatiens, R.drawable.ternarius, R.drawable.rufocinctus, R.drawable.bimaculatus,
                    R.drawable.borealis, R.drawable.vagans, R.drawable.affinis, R.drawable.griseocollis,
                    R.drawable.citrinus, R.drawable.perplexus, R.drawable.pensylvanicus, R.drawable.sylvicola,
                    R.drawable.sandersoni, R.drawable.nevadensis, R.drawable.auricomus, R.drawable.terricola,
                    R.drawable.fervidus, R.drawable.flavifrons, R.drawable.occidentalis, R.drawable.melanopygus,
                    R.drawable.bifarius1, R.drawable.huntii, R.drawable.vosnesenski, R.drawable.cryptarum,
                    R.drawable.mixtus, R.drawable.centralis
            };
            return resources[this.ordinal()];
        }
    } // Missing bohemicus


    public enum Habitat {
        Default, back_yard, balcony_container_garden, community_garden, city_park, rural,
        golf_course, roadside, woodland, farmland, school_grounds, other;

        @Override
        public String toString() {
            switch (this) {
                case Default:
                    return "";
                case back_yard:
                    return "Back Yard";
                case balcony_container_garden:
                    return "Balcony/Container Garden";
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
                    return "Other";
                default:
                    return "There was an error.";
            }
        }
    }

    public enum Weather {
        sunny, partly_cloudy, cloudy, rain;

        @Override
        public String toString() {
            switch (this) {
                case sunny:
                    return "Sunny";
                case partly_cloudy:
                    return "Partly Cloudy";
                case cloudy:
                    return "Cloudy";
                case rain:
                    return "Rainy";
                default:
                    return "There was an error.";
            }
        }

        public int getResource() {
            switch (this) {
                case sunny:
                    return R.mipmap.weather_sunny;
                case partly_cloudy:
                    return R.mipmap.weather_partly_cloudy;
                case cloudy:
                    return R.mipmap.weather_cloudy;
                case rain:
                    return R.mipmap.weather_rainy;
                default:
                    return R.mipmap.weather_sunny;
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

    public void setSpeciesType(BeeSpeciesType isEastern) {
        mSpeciesType = isEastern;
    }

    public Species getSpecies() {
        return mSpecies;
    }

    public void setSpecies(Species species) {
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
