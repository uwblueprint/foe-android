package com.blueprint.foe.beetracker.API;

import android.text.format.DateFormat;
import android.util.Log;

import com.blueprint.foe.beetracker.Model.CompletedSubmission;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;
import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that handles interacting with the Retrofit interface.
 * It abstracts away most of the API interface
 */

public class BeeTrackerCaller {
    private static final String TAG = BeeTrackerCaller.class.toString();

    public class SignupRequest {
        @SerializedName("code")
        String code;

        SignupRequest(String code) {
            this.code = code;
        }
    }

    public class SignupResponse {
        @SerializedName("token")
        String token;

        SignupResponse(String code) {
            this.token = code;
        }

        public String getToken() {
            return token;
        }
    }

    public class Image {
        @SerializedName("file")
        String file;

        public Image(String file, String filename) {
            Log.d(TAG, file.substring(0, 50));
            this.file = file;
        }
    }

    public class Sighting {
        @SerializedName("weather")
        String weather;

        @SerializedName("habitat")
        String habitat;

        @SerializedName("date")
        String date;

        @SerializedName("species")
        String species;

        @SerializedName("image")
        Image image;

        @SerializedName("latitude")
        double latitude;

        @SerializedName("longitude")
        double longitude;

        Sighting(CurrentSubmission submission) {
            this.image = new Image(StorageAccessor.convertImageToStringForServer(submission.getBitmap()), submission.getImageFilePath());
            this.latitude = submission.getLocation().getLatLng().latitude;
            this.longitude = submission.getLocation().getLatLng().longitude;
            this.weather = submission.getWeather().name();
            this.habitat = submission.getHabitat().name();
            this.species = null;
            if (submission.getSpecies() != null) {
                this.species = submission.getSpecies().toString();
            }
            this.date = DateFormat.format("yyyy-MM-dd", (new Date()).getTime()).toString();
        }
    }

    public class SubmissionRequest {
        @SerializedName("sighting")
        Sighting sighting;

        SubmissionRequest(CurrentSubmission submission) {
            this.sighting = new Sighting(submission);
        }
    }

    // TODO: add a field to store the Google Place ID/ string for the name of the place
    public class SubmissionResponse {
        @SerializedName("id")
        int id;

        @SerializedName("weather")
        String weather;

        @SerializedName("habitat")
        String habitat;

        @SerializedName("species")
        String species;

        @SerializedName("image")
        Image image;

        @SerializedName("latitude")
        double latitude;

        @SerializedName("longitude")
        double longitude;

        @SerializedName("date")
        String date;

        @SerializedName("created_at")
        String created_at;

        @SerializedName("updated_at")
        String updated_at;

        @SerializedName("url")
        String url;

        SubmissionResponse() {}

        public int getId() {
            return id;
        }

        public CompletedSubmission getSubmission() throws ParseException{
            CompletedSubmission submission = new CompletedSubmission();
            submission.setHabitat(CurrentSubmission.Habitat.valueOf(habitat));
            submission.setWeather(CurrentSubmission.Weather.valueOf(weather));
            // TODO: figure out how to set location
            if (species != null && !species.isEmpty()) {
                submission.setSpecies(CurrentSubmission.Species.valueOf(species), CurrentSubmission.BeeSpeciesType.Eastern); // TODO store Eastern/Western
            }
            submission.setDate(java.text.DateFormat.getDateInstance().parse(date));
            return submission;
        }
    }

    public static final String API_URL = "https://foe-api.herokuapp.com/";
    public Call<SignupResponse> signup(AccessToken token) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.facebookAuth(new SignupRequest(token.getToken()));
    }

    public Call<SubmissionResponse> submit(CurrentSubmission submission, String token) throws IOException{
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.submitSighting("Token " + token, new SubmissionRequest(submission));
    }
}
