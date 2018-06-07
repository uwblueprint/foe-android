package com.blueprint.foe.beetracker.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Leverages the Retrofit library to make hitting an API seem like a method call.
 * This class defines the API interface on the Android client
 */

public interface  BeeTrackerService {
    @Headers("Content-Type: application/json")
    @POST("/auth")
    Call<BeeTrackerCaller.EmailPasswordSignupResponse> emailPasswordSignup(@Body BeeTrackerCaller.EmailPasswordSignupRequest signupRequest);

    @Headers("Content-Type: application/json")
    @POST("/auth/sign_in")
    Call<BeeTrackerCaller.EmailPasswordSigninResponse> emailPasswordAuth(@Body BeeTrackerCaller.EmailPasswordSigninRequest signinRequest);

    @POST("/auth/facebook")
    Call<BeeTrackerCaller.FacebookSigninResponse> facebookAuth(@Body BeeTrackerCaller.FacebookSigninRequest token);

    @Headers("Content-Type: application/json")
    @POST("/sightings")
    Call<BeeTrackerCaller.SubmissionResponse> submitSighting(@Header("Authorization") String authorization, @Body BeeTrackerCaller.SubmissionRequest submissionRequest);
}