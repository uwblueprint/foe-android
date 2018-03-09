package com.blueprint.foe.beetracker.API;

import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.blueprint.foe.beetracker.Model.Submission;
import com.facebook.AccessToken;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that handles interacting with the Retrofit interface.
 * It abstracts away most of the API interface
 */

public class BeeTrackerCaller {

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

        @SerializedName("filename")
        String filename;

        public Image(String file, String filename) {
            this.file = file;
            this.filename = filename;
        }
    }

    public class Location {
        @SerializedName("latitude")
        double latitude;

        @SerializedName("longitude")
        double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public class Sighting {
        @SerializedName("weather")
        String weather;

        @SerializedName("habitat")
        String habitat;

        @SerializedName("species")
        String species;

        @SerializedName("image")
        Image image;

        @SerializedName("location")
        Location location;

        Sighting(Submission submission) {
            this.image = new Image(StorageAccessor.convertImageToStringForServer(submission.getBitmap()), submission.getImageFilePath());
            this.location = new Location(submission.getLocation().getLatLng().latitude, submission.getLocation().getLatLng().longitude);
            this.weather = submission.getWeather().name();
            this.habitat = submission.getHabitat().name();
            this.species = null; //submission.getSpecies().toString();
        }
    }

    public class SubmissionRequest {
        @SerializedName("sighting")
        Sighting sighting;

        SubmissionRequest(Submission submission) {
            this.sighting = new Sighting(submission);
        }
    }

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

        @SerializedName("location")
        Location location;

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

        public String getDate() {
            return date;
        }

        public String getUrl() {
            return url;
        }
    }

    public static final String API_URL = "https://foe-api.herokuapp.com/";
    public Call<SignupResponse> signup(AccessToken token) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.facebookAuth(new SignupRequest(token.getToken().toString()));
    }

    public Call<SubmissionResponse> submit(Submission submission, String token) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.submitSighting("Token " + token, new SubmissionRequest(submission));
    }
}
