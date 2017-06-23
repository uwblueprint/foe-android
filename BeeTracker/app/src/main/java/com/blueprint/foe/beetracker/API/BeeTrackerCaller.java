package com.blueprint.foe.beetracker.API;

import com.facebook.AccessToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by luisa on 2017-06-21.
 */

public class BeeTrackerCaller {
    public static final String API_URL = "https://api.github.com/";
    public Call<String> signup(AccessToken token) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        Call<String> localToken = service.facebookAuth(token.getToken().toString());
        return localToken;
    }
}
