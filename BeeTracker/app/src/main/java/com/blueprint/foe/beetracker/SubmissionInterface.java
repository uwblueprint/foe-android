package com.blueprint.foe.beetracker;

import com.blueprint.foe.beetracker.Model.Submission;

/**
 * This interface allows each fragment to interact with a common Submission object stored
 * in SubmissionActivity. It also allows fragments to tell the activity to save the current
 * submission
 */
public interface SubmissionInterface {
    void createOrResetSubmission();
    Submission getSubmission();
}
