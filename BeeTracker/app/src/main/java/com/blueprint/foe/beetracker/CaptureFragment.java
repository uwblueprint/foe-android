package com.blueprint.foe.beetracker;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;

/**
 * This fragment will allow the user to take a picture in-app.
 */
public class CaptureFragment extends Fragment {
    private static final String TAG = CaptureFragment.class.toString();
    private CameraView mCameraView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.capture_fragment, container, false);

        TextView cancelButton = (TextView) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to Home page activity
                getActivity().finish();
            }
        });

        mCameraView = (CameraView) view.findViewById(R.id.camera);

        mCameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                // Create a bitmap or a file...
                // CameraUtils will read EXIF orientation for you, in a worker thread.
                CameraUtils.decodeBitmap(picture, new CameraUtils.BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        if (checkForPermissions(getActivity())) {
                            String result = StorageAccessor.saveBitmapExternally(bitmap, getActivity());
                            if (result == null) {
                                errorAndExit("There was a problem saving your image externally.");
                            }
                            launchNextFragment();
                        } else {
                            Log.d(TAG, "checkForPermissions returned false");
                            String result = StorageAccessor.saveBitmapInternally(bitmap, getActivity());
                            if (result == null) {
                                errorAndExit("There was a problem saving your image internally.");
                            }
                            Log.d(TAG, "imaged saved internally");
                        }
                    }
                });


            }
        });

        // Add a listener to the Capture button
        ImageView captureButton = (ImageView) view.findViewById(R.id.buttonCapture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCameraView.capturePicture();
                    }
                }
        );
        return view;
    }

    private void errorAndExit(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        System.exit(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCameraView.destroy();
    }

    public void permissionResult(int[] grantResults) {
        Log.d(TAG, "request code");
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String result = StorageAccessor.readAndSaveTemporaryBitmap(getActivity());
            if (result == null) {
                errorAndExit("There was a problem saving your image externally.");
            }
        } else {
            Log.d(TAG, "Permission not granted");
            Toast.makeText(getActivity(), "You will not be able to view the image outside this app.", Toast.LENGTH_LONG).show();
        }
        launchNextFragment();
        return;
    }

    private boolean checkForPermissions(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requesting permission");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SubmissionActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            return false;
        }
        return true;
    }

    private void launchNextFragment() {
        Fragment newFragment = new IdentifyFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


}