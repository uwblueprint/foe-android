package com.blueprint.foe.beetracker.API;

import retrofit2.Call;
import retrofit2.http.Body;
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

    @Headers("Content-Type: application/json")
    @POST("/sightings")
    Call<BeeTrackerCaller.SubmissionResponse> submitSighting(@Header("access-token") String accessToken, @Header("token-type") String tokenType, @Header("client") String client, @Header("uid") String uid, @Body BeeTrackerCaller.SubmissionRequest submissionRequest);
}