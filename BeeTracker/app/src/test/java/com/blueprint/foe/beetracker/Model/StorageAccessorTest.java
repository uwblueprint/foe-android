package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests methods from StorageAcessor
 */
@RunWith(MockitoJUnitRunner.class)
public class StorageAccessorTest {

    @Mock
    Context context;

    @Mock
    FileOutputStream fos;

    @Mock
    Bitmap bitmap;

    @Test
    public void store_isCorrect() throws Exception {
        Submission submission = new Submission();
        submission.setAbdomen(0);
        submission.setThorax(0);
        submission.setFace(0);
        submission.getSpecies();
        String correctJson = "{\"mFace\":0,\"mThorax\":0,\"mAbdomen\":0,\"mSpecies\":\"Apidae\"}";
        ArgumentCaptor<byte[]> argument = ArgumentCaptor.forClass(byte[].class);

        when(context.openFileOutput("submission", Context.MODE_PRIVATE)).thenReturn(fos);

        //assertEquals("John", argument.getValue().getName());
        StorageAccessor.store(context, submission);
        verify(fos).write(argument.capture());
        String string = new String(argument.getValue());
        assertEquals(correctJson, string);
    }

    @Test
    public void load_isCorrect() throws Exception {
        Submission correctSubmission = new Submission();
        correctSubmission.setAbdomen(0);
        correctSubmission.setThorax(0);
        correctSubmission.setFace(0);
        correctSubmission.getSpecies();
        String inputJson = "{\"mFace\":0,\"mThorax\":0,\"mAbdomen\":0,\"mSpecies\":\"Apidae\"}";
        InputStream fis = new ByteArrayInputStream(inputJson.getBytes(StandardCharsets.UTF_8));

        Submission submission = StorageAccessor.load(fis);
        assertTrue(correctSubmission.equals(submission));
    }

    @Test
    public void saveBitmapInternally_isCorrect() throws Exception {
        when(context.openFileOutput("TemporaryImageFile", Context.MODE_PRIVATE)).thenReturn(fos);
        String result = StorageAccessor.saveBitmapInternally(bitmap, context);
        verify(bitmap, times(1)).compress(Bitmap.CompressFormat.JPEG, 100, fos);
        assertEquals("TemporaryImageFile", result);
    }
}
