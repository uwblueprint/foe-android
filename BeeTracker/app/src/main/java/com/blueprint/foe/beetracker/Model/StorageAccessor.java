package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.util.Log;

import com.blueprint.foe.beetracker.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Surfaces methods to store and load data into internal storage.
 * Should be used to store submission models throughout the SubmissionActivity lifecycle.
 * Should also be used to store facts throughout the LearnActivity lifecycle.
 */

public class StorageAccessor {
    private static final String TAG = StorageAccessor.class.toString();
    private static final String FILENAME = "submission";
    private static final String FACTS_FILENAME = "facts";
    private static final String STATIC_FACTS_FILENAME = "facts";

    public void storeSubmission(Context context, Submission submission) throws IOException {
        Gson gson = new Gson();
        String string = gson.toJson(submission);
        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(string.getBytes());
        fos.close();
    }

    public void storeFacts(Context context, FactCollection facts) throws IOException {
        Gson gson = new Gson();
        String string = gson.toJson(facts);
        FileOutputStream fos = context.openFileOutput(FACTS_FILENAME, Context.MODE_PRIVATE);
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

    public Submission loadSubmission(InputStream inputStream) throws IOException {
        String string = convertStreamToString(inputStream);
        inputStream.close();
        Gson gson = new Gson();
        return gson.fromJson(string, Submission.class);
    }

    public Submission loadSubmission(Context context) throws IOException {
        InputStream fis = context.openFileInput(FILENAME);
        return loadSubmission(fis);
    }

    public FactCollection loadFacts(InputStream inputStream) throws IOException {
        String string = convertStreamToString(inputStream);
        inputStream.close();
        Gson gson = new Gson();
        return gson.fromJson(string, FactCollection.class);
    }

    private boolean fileExists(Context context, String fname){
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
    public FactCollection loadFacts(Context context) throws IOException {
        InputStream fis;
        if (fileExists(context, FACTS_FILENAME)) {
            fis = context.openFileInput(FACTS_FILENAME);
        } else {
            fis = context.getResources().openRawResource(R.raw.facts);
        }
        return loadFacts(fis);
    }
}
