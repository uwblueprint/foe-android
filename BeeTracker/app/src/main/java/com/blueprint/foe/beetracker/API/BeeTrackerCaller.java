package com.blueprint.foe.beetracker.API;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import com.blueprint.foe.beetracker.Model.CompletedSubmission;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;

import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.text.ParseException;
import com.blueprint.foe.beetracker.Model.Submission;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
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

    public class SignUpRequest {
        @SerializedName("name")
        String name;

        @SerializedName("email")
        String email;

        @SerializedName("password")
        String password;

        @SerializedName("confirm_success_url")
        String successUrl;

        SignUpRequest(String name, String email, String password, String successUrl) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.successUrl = successUrl;
        }
    }

    public class SignUpResponse {
        // Only the response code matters for sign up
    }

    public class LogInRequest {
        @SerializedName("email")
        String email;

        @SerializedName("password")
        String password;

        LogInRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public class LogInResponse {
        @SerializedName("success")
        boolean success;

        @SerializedName("errors")
        List<String> errors;

        public String getConfirmationError() {
            for (String error: errors) {
                if (error.toLowerCase().contains("confirmation email")) {
                    return error;
                }
            }
            return null;
        }
    }

    public class ResendEmailRequest {
        @SerializedName("email")
        String email;

        public ResendEmailRequest(String email) {
            this.email = email;
        }
    }

    public class ResendEmailResponse {
        // Don't need anything from the response
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

        @SerializedName("street_address")
        String street_address;

        Sighting(CurrentSubmission submission) {
            this.image = new Image(StorageAccessor.convertImageToStringForServer(submission.getBitmap()), submission.getImageFilePath());
            this.latitude = submission.getLatitude();
            this.longitude = submission.getLongitude();
            this.street_address = submission.getStreetAddress();
            this.weather = submission.getWeather().name().toLowerCase();
            if (submission.getHabitat() == Submission.Habitat.balcony_container_garden) {
                this.habitat = "balcony/container_garden";
            } else {
                this.habitat = submission.getHabitat().name().toLowerCase();
            }
            this.species = null;
            if (submission.getSpecies() != null) {
                this.species = "bombus_" + submission.getSpecies().toString().toLowerCase();
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

    public class SubmissionResponse {
        @SerializedName("id")
        int id;

        @SerializedName("weather")
        String weather;

        @SerializedName("habitat")
        String habitat;

        @SerializedName("species")
        String species;

        @SerializedName("image_url")
        String image_url;

        @SerializedName("latitude")
        double latitude;

        @SerializedName("longitude")
        double longitude;

        @SerializedName("street_address")
        String street_address;

        @SerializedName("date")
        String date;

        @SerializedName("created_at")
        String created_at;

        @SerializedName("updated_at")
        String updated_at;

        @SerializedName("url")
        String url;

        SubmissionResponse() {}

        public CompletedSubmission getSubmission() throws ParseException{
            CompletedSubmission submission = new CompletedSubmission();
            submission.setHabitat(CurrentSubmission.Habitat.valueOf(habitat));
            submission.setWeather(CurrentSubmission.Weather.valueOf(weather));
            submission.setLocation(street_address, latitude, longitude);
            submission.setImageUrl(image_url);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            submission.setDate(format.parse(date));
            if (species != null && species.length() > 7) {
                submission.setSpecies(CurrentSubmission.Species.valueOf(species.substring(7)));
            }
            return submission;
        }
    }

    public static final String API_URL = "https://foe-api.herokuapp.com/";
    public static final String DEFAULT_SIGNUP_SUCCESS_URL = "http://foecanada.org/";

    public Call<SignUpResponse> signUp(String name, String email, String password) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.signUp(new SignUpRequest(name, email, password, DEFAULT_SIGNUP_SUCCESS_URL));
    }

    public Call<LogInResponse> logIn(String email, String password) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.logIn(new LogInRequest(email, password));
    }

    public Call<ResendEmailResponse> resendEmail(String email) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);

        return service.resendEmail(new ResendEmailRequest(email));
    }

    public Call<SubmissionResponse> submit(CurrentSubmission submission, String accessToken, String tokenType, String client, String uid) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.submitSighting(accessToken, tokenType, client, uid, new SubmissionRequest(submission));
    }

    public Call<SubmissionResponse[]> getAllSightings(String accessToken, String tokenType, String client, String uid) throws IOException{
         Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.getSightings(accessToken, tokenType, client, uid);
    }

    @NonNull
    private OkHttpClient getOkHttpClient() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .addInterceptor(logging)
                .build();
    }
}
