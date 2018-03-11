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
        checkForPermissions(getActivity());

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
                    public void onBitmapReady(final Bitmap bitmap) {
                        saveAndLaunchNextFragment(bitmap);
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

    // Get the user to select an image from their gallery. The return is handled in onActivityResult
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

    // Gets triggered when the user has chosen to grant permission or not
    public void permissionResult(int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            errorAndExit("The app cannot continue without permission to save externally.");
        }
    }

    // Verify whether the user has enabled the permission required for CaptureFragment
    // If not, launch the request for this permission. The return from this request is handled in
    // permissionResult above
    private boolean checkForPermissions(Activity context) {
        boolean hasWritePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        if (hasWritePermission) {
            return true;
        }
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                SubmissionActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        return false;
    }

    // Store the image externally in a separate thread
    private void saveAndLaunchNextFragment(final Bitmap image) {
        final File file = StorageAccessor.getOutputMediaFile(StorageAccessor.MEDIA_TYPE_IMAGE);
        new Thread(new Runnable() {
            public void run() {
                try {
                    StorageAccessor.saveBitmapExternally(image, file);
                } catch (IOException e) {
                    // Fail silently, we don't need the image to post a submission, it's just a nice
                    // to have for the user
                    e.printStackTrace();
                }
            }
        }).start();
        saveSubmission(image, file.getAbsolutePath());
        launchNextFragment();
    }

    private void saveSubmission(Bitmap image, String filepath) {
        SubmissionInterface submissionInterface = (SubmissionInterface) getActivity();
        submissionInterface.createOrResetSubmission();
        Submission submission = submissionInterface.getSubmission();
        submission.setBitmap(image);
        submission.setImageFilePath(filepath);
    }

    private void launchNextFragment() {
        Fragment newFragment = new IdentifyFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    // The user chose to open an image from their gallery. This intent returns a URI. We need to
    // convert this URI to a bitmap
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    // This method gets triggered when the user selects a specific image from their gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            final Uri fullPhotoUri = data.getData();
            try {
                Bitmap image = getBitmapFromUri(fullPhotoUri);
                saveAndLaunchNextFragment(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}