package com.blueprint.foe.beetracker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;
import com.blueprint.foe.beetracker.Model.StorageAccessor;

import java.io.IOException;

public class SubmissionActivity extends AppCompatActivity implements SubmissionInterface, BeeAlertDialogListener {
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final String TAG = SubmissionActivity.class.toString();
    private CurrentSubmission submission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CaptureFragment fragment = new CaptureFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

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
            submission = new CurrentSubmission();
        } else {
            submission.reset();
        }
    }

    @Override
    public CurrentSubmission getSubmission() {
        return submission;
    }

    @Override
    public void onDialogFinishClick(int id) {
        if (id == ERROR_DIALOG) {
            // Do nothing
        } else if (id == NORMAL_DIALOG) {
            // Should never trigger this
            Log.e(TAG, "Normal dialog should not trigger in submission activity");
        }
    }
}
