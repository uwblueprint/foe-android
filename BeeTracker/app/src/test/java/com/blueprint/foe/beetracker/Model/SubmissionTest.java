package com.blueprint.foe.beetracker.Model;

import android.location.Location;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests all aspects of the Submission model
 */

public class SubmissionTest {
    @Test
    public void correctSpecies_isCorrect() throws Exception {
        Submission submission = new Submission();
        submission.setAbdomen(0);
        submission.setThorax(0);
        submission.setFace(0);
        Submission.Species species = submission.getSpecies();
    }

    @Test
    public void incorrectSpecies_throwsException() throws Exception {
        Submission submission = new Submission();
        submission.setFace(0);
        try {
            Submission.Species species = submission.getSpecies();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Thorax is missing", e.getMessage());
        }
    }

    @Test
    public void incorrectSpecies2_throwsException() throws Exception {
        Submission submission = new Submission();
        submission.setAbdomen(0);
        submission.setThorax(0);
        try {
            Submission.Species species = submission.getSpecies();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Face is missing", e.getMessage());
        }
    }

    @Test
    public void incorrectFace_throwsException() throws Exception {
        Submission submission = new Submission();
        try {
            submission.setFace(Submission.MAX_FACE + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid face value", e.getMessage());
        }
    }

    @Test
    public void incorrectAbdomen_throwsException() throws Exception {
        Submission submission = new Submission();
        submission.setThorax(0);
        try {
            submission.setAbdomen(Submission.MAX_ABDOMEN + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid abdomen value", e.getMessage());
        }
    }

    @Test
    public void incorrectThorax_throwsException() throws Exception {
        Submission submission = new Submission();
        try {
            submission.setThorax(Submission.MAX_THORAX + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid thorax value", e.getMessage());
        }
    }

    @Test
    public void gettersAndSetters() throws Exception {
        Submission submission = new Submission();
        int face = 0;
        int abdomen = 1;
        int thorax = 2;
        Submission.Weather weather = Submission.Weather.Cloudy;
        Submission.Habitat habitat = Submission.Habitat.Park;
        Location location = new Location("Test");

        submission.setFace(face);
        submission.setAbdomen(abdomen);
        submission.setThorax(thorax);
        submission.getSpecies();
        submission.setWeather(weather);
        submission.setHabitat(habitat);
        submission.setLocation(location);

        assertEquals(face, submission.getFace());
        assertEquals(abdomen, submission.getAbdomen());
        assertEquals(thorax, submission.getThorax());
        assertEquals(weather, submission.getWeather());
        assertEquals(habitat, submission.getHabitat());
        assertEquals(location, submission.getLocation());

    }

    @Test
    public void completeSubmission_isComplete() throws Exception {
        Submission submission = new Submission();

        submission.setFace(0);
        submission.setAbdomen(0);
        submission.setThorax(0);
        submission.getSpecies();
        submission.setWeather(Submission.Weather.Cloudy);
        submission.setHabitat(Submission.Habitat.Park);
        submission.setLocation(new Location("Test"));
        submission.setImageFilePath("filepath");

        assertEquals(true, submission.isComplete());

    }

    @Test
    public void incompleteSubmission_isIncomplete() throws Exception {
        Submission submission = new Submission();

        submission.setFace(0);
        submission.setAbdomen(0);
        submission.setThorax(0);
        submission.getSpecies();
        submission.setWeather(Submission.Weather.Cloudy);
        submission.setHabitat(Submission.Habitat.Park);
        submission.setImageFilePath("filepath");

        assertEquals(false, submission.isComplete());

    }

    @Test
    public void submission_isEqual() throws Exception {
        Submission submission = new Submission();

        submission.setFace(0);
        submission.setAbdomen(0);
        submission.setThorax(0);
        submission.getSpecies();
        submission.setWeather(Submission.Weather.Cloudy);
        submission.setHabitat(Submission.Habitat.Park);

        Submission submission1 = new Submission();
        submission1.setFace(0);
        submission1.setAbdomen(0);
        submission1.setThorax(0);
        submission1.getSpecies();
        submission1.setWeather(Submission.Weather.Cloudy);
        submission1.setHabitat(Submission.Habitat.Park);

        assertTrue(submission.equals(submission1));
        assertTrue(submission1.equals(submission));
    }

    @Test
    public void submission_isNotEqual() throws Exception {
        Submission submission = new Submission();

        submission.setFace(0);
        submission.setAbdomen(0);
        submission.setThorax(0);
        submission.setHabitat(Submission.Habitat.Park);

        Submission submission1 = new Submission();
        submission1.setFace(0);
        submission1.setAbdomen(0);
        submission1.setThorax(0);
        submission1.setWeather(Submission.Weather.Cloudy); // other has no weather
        submission1.setHabitat(Submission.Habitat.Park);

        assertFalse(submission.equals(submission1));
        assertFalse(submission1.equals(submission));
    }
}
