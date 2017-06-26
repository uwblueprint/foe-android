package com.blueprint.foe.beetracker.Model;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Surfaces methods to store and load data into internal storage.
 * Should be used to store submission models throughout the SubmissionActivity lifecycle.
 */

public class StorageAccessor {
    private static final String FILENAME = "submission";

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

    public Submission load(Context context) throws IOException {
        FileInputStream fis = context.openFileInput(FILENAME);
        String string = convertStreamToString(fis);
        fis.close();
        Gson gson = new Gson();
        return gson.fromJson(string, Submission.class);
    }
}
