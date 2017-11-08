package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Surfaces methods to store and load data into internal storage.
 * Should be used to store submission models throughout the SubmissionActivity lifecycle.
 */

public class StorageAccessor {
    private static final String TAG = StorageAccessor.class.toString();
    private static final String FILENAME = "submission";
    private static final String TEMPORARY_IMAGE_FILENAME = "TemporaryImageFile";


    public void store(Context context, Submission submission) throws IOException {
        Gson gson = new Gson();
        String string = gson.toJson(submission);
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public Submission load(InputStream inputStream) throws IOException {
        String string = convertStreamToString(inputStream);
        inputStream.close();
        Gson gson = new Gson();
        return gson.fromJson(string, Submission.class);
    }

    public Submission load(Context context) throws IOException {
        InputStream fis = context.openFileInput(FILENAME);
        return load(fis);
    }

    public static String saveBitmapExternally(Bitmap bitmap, Context context) {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        Log.d(TAG, pictureFile.getPath());
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            context.deleteFile(TEMPORARY_IMAGE_FILENAME);
            return pictureFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readAndSaveTemporaryBitmap(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(TEMPORARY_IMAGE_FILENAME);
            byte arr[] = new byte[(int) fis.getChannel().size()];
            fis.read(arr);
            Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
            return saveBitmapExternally(image, context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String saveBitmapInternally(Bitmap bitmap, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(TEMPORARY_IMAGE_FILENAME, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return TEMPORARY_IMAGE_FILENAME;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BumbleBeeCount");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("BeeTracker", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
