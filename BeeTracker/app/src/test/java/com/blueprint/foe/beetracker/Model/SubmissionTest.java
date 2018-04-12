package com.blueprint.foe.beetracker.Model;

import android.graphics.Bitmap;

import com.google.android.gms.location.places.Place;

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
        Submission submission = new CurrentSubmission();
        CurrentSubmission.Weather weather = CurrentSubmission.Weather.Cloudy;
        CurrentSubmission.Habitat habitat = CurrentSubmission.Habitat.Back_Yard;

        submission.setSpecies(CurrentSubmission.Species.affinis, CurrentSubmission.BeeSpeciesType.Eastern);
        submission.setWeather(weather);
        submission.setHabitat(habitat);
        submission.setLocation(place);

        assertEquals(CurrentSubmission.Species.affinis, submission.getSpecies());
        assertEquals(CurrentSubmission.BeeSpeciesType.Eastern, submission.getSpeciesType());
        assertEquals(weather, submission.getWeather());
        assertEquals(habitat, submission.getHabitat());
        assertEquals(place, submission.getLocation());

    }

    @Test
    public void completeSubmission_isComplete() throws Exception {
        CurrentSubmission submission = new CurrentSubmission();

        submission.setBitmap(bitmap);
        submission.setWeather(CurrentSubmission.Weather.Cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.Back_Yard);
        submission.setLocation(place);

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
