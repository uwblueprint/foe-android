package com.blueprint.foe.beetracker;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import com.blueprint.foe.beetracker.Model.Submission;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This fragment will allow the user to take a picture in-app.
 */
public class CaptureFragment extends Fragment {
    private static final String TAG = CaptureFragment.class.toString();
    static final int REQUEST_IMAGE_GET = 1;

    private CameraView mCameraView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.capture_fragment, container, false);

        TextView cancelTextView = (TextView) view.findViewById(R.id.cancelButton);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
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
                // CameraUtils will generate image, with correct EXIF orientation, in a worker thread.
                CameraUtils.decodeBitmap(picture, new CameraUtils.BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        if (checkForPermissions(getActivity())) {
                            String filename = null;
                            try {
                                filename = StorageAccessor.saveBitmapExternally(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                errorAndExit("There was a problem saving your image externally.");
                            }
                            saveAndLaunchNextFragment(filename);
                        } else {
                            // Once permission is granted, image will be read from internal storage and saved externally
                            try {
                                StorageAccessor.saveBitmapInternally(bitmap, getActivity());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                errorAndExit("There was a problem saving your image internally.");
                            }
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

        ImageView libraryButton = (ImageView) view.findViewById(R.id.buttonLibrary);
        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        return view;
    }

    private void errorAndExit(String message) {
        getActivity().finish();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
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
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String filename = null;
            try {
                Bitmap image = StorageAccessor.readInternalBitmap(getActivity());
                filename = StorageAccessor.saveBitmapExternally(image);
                StorageAccessor.deleteInternalBitmap(getActivity());
            } catch (IOException e) {
                e.printStackTrace();
                errorAndExit("There was a problem saving your image externally.");
            }
            saveAndLaunchNextFragment(filename);
        } else {
            StorageAccessor.deleteInternalBitmap(getActivity());
            errorAndExit("The app cannot continue without permission to save externally.");
        }
    }

    private boolean checkForPermissions(Activity context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "BeeTracker needs permission to save to storage so you can access the image later.", Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    SubmissionActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            return false;
        }
        return true;
    }

    private void saveAndLaunchNextFragment(String imageFilePath) {
        saveSubmission(imageFilePath);
        launchNextFragment();
    }

    private void saveSubmission(String imageFilePath) {
        SubmissionInterface submissionInterface = (SubmissionInterface) getActivity();
        submissionInterface.createOrResetSubmission();
        Submission submission = submissionInterface.getSubmission();
        submission.setImageFilePath(imageFilePath);
    }

    private void launchNextFragment() {
        Fragment newFragment = new IdentifyFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            String filename = "";
            try {
                Bitmap image = getBitmapFromUri(fullPhotoUri);
                filename = StorageAccessor.saveBitmapExternally(image);
            } catch (IOException e) {
                e.printStackTrace();
                errorAndExit("There was a problem saving your image externally.");
            }
            saveAndLaunchNextFragment(filename);
        }
    }

}