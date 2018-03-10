package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.location.places.Place;

import org.junit.Assert;
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
    @Mock
    Place place;

    @Mock
    Bitmap bitmap;

    @Test
    public void gettersAndSetters() throws Exception {
        Submission submission = new Submission();
        Submission.Weather weather = Submission.Weather.Cloudy;
        Submission.Habitat habitat = Submission.Habitat.Park;

        submission.setSpecies(Submission.Species.affinis, Submission.BeeSpeciesType.Eastern);
        submission.setWeather(weather);
        submission.setHabitat(habitat);
        submission.setLocation(place);

        assertEquals(Submission.Species.affinis, submission.getSpecies());
        assertEquals(Submission.BeeSpeciesType.Eastern, submission.getSpeciesType());
        assertEquals(weather, submission.getWeather());
        assertEquals(habitat, submission.getHabitat());
        assertEquals(place, submission.getLocation());

    }

    @Test
    public void completeSubmission_isComplete() throws Exception {
        Submission submission = new Submission();

        submission.setBitmap(bitmap);
        submission.setWeather(Submission.Weather.Cloudy);
        submission.setHabitat(Submission.Habitat.Park);
        submission.setLocation(place);

        assertEquals(true, submission.isComplete()); // Species is not needed for completeness
    }

    @Test
    public void incompleteSubmission_isIncomplete() throws Exception {
        Submission submission = new Submission();

        submission.setBitmap(bitmap);
        submission.setWeather(Submission.Weather.Cloudy);
        submission.setHabitat(Submission.Habitat.Park);

        assertEquals(false, submission.isComplete());
    }

    @Test
    public void submission_isEqual() throws Exception {
        Submission submission = new Submission();

        submission.setWeather(Submission.Weather.Cloudy);
        submission.setHabitat(Submission.Habitat.Park);
        submission.setSpecies(Submission.Species.affinis, Submission.BeeSpeciesType.Eastern);

        Submission submission1 = new Submission();

        submission1.setWeather(Submission.Weather.Cloudy);
        submission1.setHabitat(Submission.Habitat.Park);
        submission1.setSpecies(Submission.Species.affinis, Submission.BeeSpeciesType.Eastern);

        assertTrue(submission.equals(submission1));
        assertTrue(submission1.equals(submission));
    }

    @Test
    public void submission_isNotEqual() throws Exception {
        Submission submission = new Submission();

        submission.setHabitat(Submission.Habitat.Park);

        Submission submission1 = new Submission();
        submission1.setWeather(Submission.Weather.Cloudy); // other has no weather
        submission1.setHabitat(Submission.Habitat.Park);

        assertFalse(submission.equals(submission1));
        assertFalse(submission1.equals(submission));
    }
}
