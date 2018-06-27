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
import com.blueprint.foe.beetracker.Exceptions.EmptyCredentialsException;
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

    public class EmailPasswordSignupRequest {
        @SerializedName("email")
        String email;

        @SerializedName("password")
        String password;

        @SerializedName("confirm_success_url")
        String successUrl;

        EmailPasswordSignupRequest(String email, String password, String successUrl) {
            this.email = email;
            this.password = password;
            this.successUrl = successUrl;
        }
    }

    public class EmailPasswordSignupResponse {
        // Only the response code matters for sign up
    }

    public class EmailPasswordSigninRequest {
        @SerializedName("email")
        String email;

        @SerializedName("password")
        String password;

        EmailPasswordSigninRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public class EmailPasswordSigninResponse {
        // Only the response headers matter for sign in
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
            this.weather = submission.getWeather().name();
            this.habitat = submission.getHabitat().name();
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
        public String weather;

        @SerializedName("habitat")
        public String habitat;

        @SerializedName("species")
        public String species;

        @SerializedName("image_url")
        public String image_url;

        @SerializedName("latitude")
        double latitude;

        @SerializedName("longitude")
        double longitude;

        @SerializedName("street_address")
        public String street_address;

        @SerializedName("date")
        public String date;

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
            if (species != null && !species.isEmpty()) {
                submission.setSpecies(CurrentSubmission.Species.valueOf(species), CurrentSubmission.BeeSpeciesType.Eastern); // TODO store Eastern/Western
            }
            submission.setDate(java.text.DateFormat.getDateInstance().parse(date));
            return submission;
        }
    }

    public static final String API_URL = "https://foe-api.herokuapp.com/";
    public static final String DEFAULT_SIGNUP_SUCCESS_URL = "http://foecanada.org/";

    public Call<EmailPasswordSignupResponse> emailPasswordSignup(String email, String password) throws IOException, EmptyCredentialsException{
        if (email.isEmpty() || password.isEmpty()) {
            throw new EmptyCredentialsException();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.emailPasswordSignup(new EmailPasswordSignupRequest(email, password, DEFAULT_SIGNUP_SUCCESS_URL));
    }

    public Call<EmailPasswordSigninResponse> emailPasswordSignin(String email, String password) throws IOException, EmptyCredentialsException{
        if (email.isEmpty() || password.isEmpty()) {
            throw new EmptyCredentialsException();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);

        return service.emailPasswordAuth(new EmailPasswordSigninRequest(email, password));
    }

    public Call<SubmissionResponse> submit(CurrentSubmission submission, String accessToken, String tokenType, String client, String uid) throws IOException{
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
        return service.submitSighting(accessToken, tokenType, client, uid, new SubmissionRequest(submission));
    }

    public Call<SubmissionResponse[]> getAllSightings(String accessToken, String tokenType, String client, String uid) throws IOException{
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
