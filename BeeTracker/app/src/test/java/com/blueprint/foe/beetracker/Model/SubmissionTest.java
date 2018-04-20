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
        CurrentSubmission.Weather weather = CurrentSubmission.Weather.cloudy;
        CurrentSubmission.Habitat habitat = CurrentSubmission.Habitat.back_yard;

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
        submission.setWeather(CurrentSubmission.Weather.cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.back_yard);
        submission.setLocation(place);

        assertEquals(true, submission.isComplete()); // Species is not needed for completeness
    }

    @Test
    public void incompleteSubmission_isIncomplete() throws Exception {
        CurrentSubmission submission = new CurrentSubmission();

        submission.setBitmap(bitmap);
        submission.setWeather(CurrentSubmission.Weather.cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.back_yard);
        // Missing Location

        assertEquals(false, submission.isComplete());
    }

    @Test
    public void submission_isEqual() throws Exception {
        CurrentSubmission submission = new CurrentSubmission();

        submission.setWeather(CurrentSubmission.Weather.cloudy);
        submission.setHabitat(CurrentSubmission.Habitat.back_yard);
        submission.setSpecies(CurrentSubmission.Species.affinis, CurrentSubmission.BeeSpeciesType.Eastern);

        CurrentSubmission submission1 = new CurrentSubmission();
        submission1.setWeather(CurrentSubmission.Weather.cloudy);
        submission1.setHabitat(CurrentSubmission.Habitat.back_yard);
        submission1.setSpecies(CurrentSubmission.Species.affinis, CurrentSubmission.BeeSpeciesType.Eastern);

        assertTrue(submission.equals(submission1));
        assertTrue(submission1.equals(submission));
    }

    @Test
    public void submission_isNotEqual() throws Exception {
        Submission submission = new CurrentSubmission();

        submission.setHabitat(CurrentSubmission.Habitat.back_yard);

        Submission submission1 = new CurrentSubmission();
        submission1.setWeather(CurrentSubmission.Weather.cloudy); // other has no weather
        submission1.setHabitat(CurrentSubmission.Habitat.back_yard);

        assertFalse(submission.equals(submission1));
        assertFalse(submission1.equals(submission));
    }
}
