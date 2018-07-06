package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.blueprint.foe.beetracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Surfaces methods to store and load data into internal storage.
 * Should be used to store submission models throughout the SubmissionActivity lifecycle.
 * Should also be used to store facts throughout the LearnActivity lifecycle.
 */

public class StorageAccessor {
    private static final String TAG = StorageAccessor.class.toString();
    private static final String FILENAME = "submission";
    private static final String FACTS_FILENAME = "facts";
    private static final String SIGHTINGS_FILENAME = "sightings";
    public static final int MEDIA_TYPE_IMAGE = 1;

    public static void storeSubmission(Context context, Submission submission) throws IOException {
        Gson gson = new Gson();
        String string = gson.toJson(submission);
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }

    public static void storeFacts(Context context, FactCollection facts) throws IOException {
        Gson gson = new Gson();
        String string = gson.toJson(facts);
        FileOutputStream fos = context.openFileOutput(FACTS_FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }

    public static void storeSightings(Context context, List<CompletedSubmission> submissions) throws IOException {
        Gson gson = new Gson();
        String string = gson.toJson(submissions);
        FileOutputStream fos = context.openFileOutput(SIGHTINGS_FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static Submission loadSubmission(InputStream inputStream) throws IOException {
        String string = convertStreamToString(inputStream);
        inputStream.close();
        Gson gson = new Gson();
        return gson.fromJson(string, CurrentSubmission.class);
    }

    public static Submission loadSubmission(Context context) throws IOException {
        InputStream fis = context.openFileInput(FILENAME);
        return loadSubmission(fis);
    }

    public static FactCollection loadFacts(InputStream inputStream) throws IOException {
        String string = convertStreamToString(inputStream);
        inputStream.close();
        Gson gson = new Gson();
        return gson.fromJson(string, FactCollection.class);
    }

    public static List<CompletedSubmission> loadSightings(Context context) throws IOException {
        InputStream fis;
        if (fileExists(context, SIGHTINGS_FILENAME)) {
            fis = context.openFileInput(SIGHTINGS_FILENAME);
            String string = convertStreamToString(fis);
            fis.close();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<CompletedSubmission>>(){}.getType();
            return gson.fromJson(string, listType);
        } else {
            return new ArrayList<>();
        }
    }

    private static boolean fileExists(Context context, String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }

    /*
     * To maintain state of which facts have been completed, we store the FactCollection in a file
     * in internal storage. If the file exists, then we load the FactCollection from it.
     * Otherwise, it is the first time launch of the Learn More activity, and we load the facts
     * from the raw facts resource file.
     *
     * In the future we want the first-time load to pull from our server. We also want to add
     * update functionality, where our app will continuously poll the server for new facts.
     */
    public static FactCollection loadFacts(Context context) throws IOException {
        InputStream fis;
        if (fileExists(context, FACTS_FILENAME)) {
            fis = context.openFileInput(FACTS_FILENAME);
        } else {
            fis = context.getResources().openRawResource(R.raw.facts);
        }
        return loadFacts(fis);
    }

    public static String saveBitmapExternally(Bitmap bitmap, File pictureFile) throws FileNotFoundException {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return pictureFile.getAbsolutePath();
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

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BumbleBeeCount");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.e(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String convertImageToStringForServer(Bitmap imageBitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (imageBitmap != null) {
            int width = 500;
            double ratio = (double) imageBitmap.getWidth() / imageBitmap.getHeight();
            int height = (int) (width / ratio);
            Bitmap compressedBmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
            compressedBmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
            byte[] byteArray = stream.toByteArray();
            String encodedString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            Log.d(TAG, encodedString.substring(0, 50));
            return "data:image/png;base64," + encodedString;
        } else {
            return null;
        }
    }
}
