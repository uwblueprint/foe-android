package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests all aspects of the Submission model
 */
@RunWith(MockitoJUnitRunner.class)
public class SubmissionTest {
    private static double DELTA = 0.00001; // for asserting double equality

    @Mock
    Bitmap bitmap;

    @Test
    public void gettersAndSetters() throws Exception {
        Submission submission = new CurrentSubmission();
        CurrentSubmission.Weather weather = CurrentSubmission.Weather.Cloudy;
        CurrentSubmission.Habitat habitat = CurrentSubmission.Habitat.Back_Yard;
        String address = "University of Waterloo";
        double latitude = 45.1;
        double longitude = 47.2;

        submission.setSpecies(CurrentSubmission.Species.affinis, CurrentSubmission.BeeSpeciesType.Eastern);
        submission.setWeather(weather);
        submission.setHabitat(habitat);
        submission.setLocation(address, latitude, longitude);

        assertEquals(CurrentSubmission.Species.affinis, submission.getSpecies());
        assertEquals(CurrentSubmission.BeeSpeciesType.Eastern, submission.getSpeciesType());
        assertEquals(weather, submission.getWeather());
        assertEquals(habitat, submission.getHabitat());
        assertEquals(address, submission.getStreetAddress());
        assertEquals(latitude, submission.getLatitude(), DELTA);
        assertEquals(longitude, submission.getLongitude(), DELTA);
    }

    @Test
    public void completeSubmission_isComplete() throws Exception {
        String address = "University of Waterloo";
        double latitude = 45.1;
        double longitude = 47.2;

        CurrentSubmission submission = new CurrentSubmission();

        submission.setBitmap(bitmap);
        submission.setWeather(CurrentSubmission.Weather.Cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.Back_Yard);
        submission.setLocation(address, latitude, longitude);

        assertEquals(true, submission.isComplete()); // Species is not needed for completeness
    }

    @Test
    public void incompleteSubmission_isIncomplete() throws Exception {
        CurrentSubmission submission = new CurrentSubmission();

        submission.setBitmap(bitmap);
        submission.setWeather(CurrentSubmission.Weather.Cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.Back_Yard);
        // Missing Location

        assertEquals(false, submission.isComplete());
    }

    @Test
    public void submission_isEqual() throws Exception {
        CurrentSubmission submission = new CurrentSubmission();

        submission.setWeather(CurrentSubmission.Weather.Cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.Back_Yard);
        submission.setSpecies(CurrentSubmission.Species.affinis, CurrentSubmission.BeeSpeciesType.Eastern);

        CurrentSubmission submission1 = new CurrentSubmission();
        submission1.setWeather(CurrentSubmission.Weather.Cloudy);
        submission1.setHabitat(CurrentSubmission.Habitat.Back_Yard);
        submission1.setSpecies(CurrentSubmission.Species.affinis, CurrentSubmission.BeeSpeciesType.Eastern);

        assertTrue(submission.equals(submission1));
        assertTrue(submission1.equals(submission));
    }

    @Test
    public void submission_isNotEqual() throws Exception {
        Submission submission = new CurrentSubmission();

        submission.setHabitat(CurrentSubmission.Habitat.Back_Yard);

        Submission submission1 = new CurrentSubmission();
        submission1.setWeather(CurrentSubmission.Weather.Cloudy); // other has no weather
        submission1.setHabitat(CurrentSubmission.Habitat.Back_Yard);

        assertFalse(submission.equals(submission1));
        assertFalse(submission1.equals(submission));
    }
}
