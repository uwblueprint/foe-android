package com.blueprint.foe.beetracker.Model;

import org.junit.Assert;
import org.junit.Test;

import java.util.MissingFormatArgumentException;

import static org.junit.Assert.assertEquals;

/**
 * Created by luisa on 2017-06-24.
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
        } catch (MissingFormatArgumentException e) {
            assertEquals("Format specifier 'Thorax is missing'", e.getMessage());
        }
    }

    @Test
    public void incorrectFace_throwsException() throws Exception {
        Submission submission = new Submission();
        try {
            submission.setAbdomen(Submission.MAX_FACE + 1);
            Assert.fail();
        } catch (MissingFormatArgumentException e) {
            assertEquals("Format specifier 'Invalid abdomen value'", e.getMessage());
        }
    }
}
