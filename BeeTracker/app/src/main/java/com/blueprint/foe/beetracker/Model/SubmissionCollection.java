package com.blueprint.foe.beetracker.Model;

import java.util.ArrayList;

/**
 * This class wraps an ArrayList of submissions.
 * The primary purpose of this class is to leverage Gson when storing/loading.
 * Gson expects a class as input, hence this wrapper.
 *
 * In the future we may want to leverage this class to help with appending new submissions.
 */

public class SubmissionCollection {

    private ArrayList<CompletedSubmission> submissions;

    public SubmissionCollection() {
        this.submissions = new ArrayList<>();
    }

    public void addSubmission(CompletedSubmission submission) {
        this.submissions.add(submission);
    }

    public ArrayList<CompletedSubmission> getSubmissions() {
        return this.submissions;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof SubmissionCollection)) {
            return false;
        }
        if (other == this) return true;

        SubmissionCollection that = (SubmissionCollection) other;

        return this.submissions.equals(that.submissions);
    }
}
