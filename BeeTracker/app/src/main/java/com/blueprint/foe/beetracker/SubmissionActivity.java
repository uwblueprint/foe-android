package com.blueprint.foe.beetracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.blueprint.foe.beetracker.Model.Submission;

public class SubmissionActivity extends AppCompatActivity implements SubmissionInterface {
    public static final String SHARED_PREFS = "SubmissionSharedPrefs";
    private Submission submission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CaptureFragment fragment = new CaptureFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    };

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public Submission getSubmission() {
        return submission;
    }
}
