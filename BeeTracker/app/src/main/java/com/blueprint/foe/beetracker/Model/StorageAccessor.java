package com.blueprint.foe.beetracker.Model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by luisa on 2017-06-26.
 */

public class StorageAccessor {
    private static final String FILENAME = "submission";

    public void store(Context context, Submission submission) throws FileNotFoundException, IOException {
        String string = "hello world!";
        // Gson gson = new Gson();
        // string = gson.toJson(submission)
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

    public Submission load(Context context) throws FileNotFoundException, IOException {
        FileInputStream fis = context.openFileInput(FILENAME);
        String string = convertStreamToString(fis);
        fis.close();
        //Submission submission = gson.fromJson(string, Submission.class);
        return null;
    }
}
