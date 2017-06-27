package com.blueprint.foe.beetracker.Model;

import android.location.Location;

import java.util.MissingFormatArgumentException;

/**
 * Represents the data object that contains all information related to submission.
 */

public class Submission {
    public static final int MAX_FACE = 3;
    public static final int MAX_THORAX = 3;
    public static final int MAX_ABDOMEN = 3;

    private int face; // the index of the image chosen by user
    private int thorax; // the index of the image chosen by user
    private int abdomen; // the index of the image chosen by user
    private Species species = null;
    private Habitat habitat = null;
    private Weather weather = null;
    private Location location = null;
    // TODO: Store image in camera role. https://github.com/uwblueprint/foe/issues/22

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Submission)) {
            return false;
        }

        Submission that = (Submission) other;

        return this.face == that.face
                && this.thorax == that.thorax
                && this.abdomen == that.abdomen
                && this.species == that.species
                && this.habitat == that.habitat
                && this.weather == that.weather
                && ((this.location == null && that.location == null) || this.location.equals(that.location));
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

    public void clear() {
        face = -1;
        thorax = -1;
        abdomen = -1;
        species = null;
        habitat = null;
        weather = null;
        location = null;
    }

    public Submission() {
        clear();
    }

    public void setFace(int f) throws MissingFormatArgumentException {
        if (f > MAX_FACE) {
            throw new MissingFormatArgumentException("Invalid face value");
        }
        face = f;
    }

    public int getFace() {
        return face;
    }

    public void setThorax(int t) throws MissingFormatArgumentException {
        if (t > MAX_THORAX) {
            throw new MissingFormatArgumentException("Invalid thorax value");
        }
        thorax = t;
    }

    public int getThorax() {
        return thorax;
    }

    public void setAbdomen(int a) throws MissingFormatArgumentException {
        if (a > MAX_ABDOMEN) {
            throw new MissingFormatArgumentException("Invalid abdomen value");
        }
        abdomen = a;
    }

    public int getAbdomen() {
        return abdomen;
    }

    public void setHabitat(Habitat h) {
        habitat = h;
    }

    public Habitat getHabitat() {
        return habitat;
    }

    public void setWeather(Weather w) {
        weather = w;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Species getSpecies() throws MissingFormatArgumentException {
        if (species == null) {
            findSpecies();
        }
        return species;
    }

    public void findSpecies() throws MissingFormatArgumentException {
        if (face < 0) {
            throw new MissingFormatArgumentException("Face is missing");
        } else if (thorax < 0) {
            throw new MissingFormatArgumentException("Thorax is missing");
        } else if (abdomen < 0) {
            throw new MissingFormatArgumentException("Abdomen is missing");
        } else if (face > MAX_FACE) {
            throw new MissingFormatArgumentException("Invalid face value");
        } else if (thorax > MAX_THORAX) {
            throw new MissingFormatArgumentException("Invalid thorax value");
        } else if (abdomen > MAX_ABDOMEN) {
            throw new MissingFormatArgumentException("Invalid abdomen value");
        }

        if (face == 0 && thorax == 0 && abdomen == 0)
            species = Species.Apidae;
        else if (face == 0 && thorax == 0 && abdomen == 1)
            species = Species.Andrenidae;
        else if (face == 0 && thorax == 1 && abdomen == 0)
            species = Species.Halictidae;
        else if (face == 0 && thorax == 1 && abdomen == 1)
            species = Species.Megachilidae;
        else if (face == 1 && thorax == 0 && abdomen == 0)
            species = Species.Colletidae;
        else
            species = Species.Unknown;
    }

    public boolean isComplete() {
        return species != null && habitat != null && weather != null && location != null;
    }

    /* For debug only */
    public void print() {
        System.out.println("Face: " + face + ", Thorax: " + thorax + ", Abdomen: " + abdomen);
        System.out.println("Species: " + species + ", Habitat: " + habitat + ", Weather: " + weather);
        System.out.println("Location: " + location);
    }
}
