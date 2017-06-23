package com.blueprint.foe.beetracker.API;

import com.facebook.AccessToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by luisa on 2017-06-21.
 */

public interface  BeeTrackerService {
    @POST("/auth/facebook")
    Call<String> facebookAuth(@Body String token);
}