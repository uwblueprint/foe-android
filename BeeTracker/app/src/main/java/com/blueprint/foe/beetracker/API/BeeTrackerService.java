package com.blueprint.foe.beetracker.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Leverages the Retrofit library to make hitting an API seem like a method call.
 * This class defines the API interface on the Android client
 */

public interface  BeeTrackerService {
    @POST("/auth/facebook")
    Call<BeeTrackerCaller.SignupResponse> facebookAuth(@Body BeeTrackerCaller.SignupRequest token);
}