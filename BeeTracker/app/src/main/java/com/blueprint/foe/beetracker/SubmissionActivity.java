package com.blueprint.foe.beetracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.blueprint.foe.beetracker.Model.Submission;

import java.io.IOException;

public class SubmissionActivity extends AppCompatActivity implements SubmissionInterface {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final String TAG = SubmissionActivity.class.toString();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                CaptureFragment fragment = (CaptureFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                fragment.permissionResult(grantResults);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            StorageAccessor.storeSubmission(this, submission);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "There was a problem storing your submission.");
            errorAndExit("There was a problem storing your submission.");
        }
    }

    private void errorAndExit(String message) {
        finish();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void createOrResetSubmission() {
        if (this.submission == null) {
            submission = new Submission();
        } else {
            submission.reset();
        }
    }

    @Override
    public Submission getSubmission() {
        return submission;
    }
}
