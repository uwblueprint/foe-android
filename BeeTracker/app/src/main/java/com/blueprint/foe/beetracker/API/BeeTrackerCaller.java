package com.blueprint.foe.beetracker.API;

import com.facebook.AccessToken;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by luisa on 2017-06-21.
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
    public static final String API_URL = "https://foe-api.herokuapp.com/";
    public Call<SignupResponse> signup(AccessToken token) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        Call<SignupResponse> localToken = service.facebookAuth(new SignupRequest(token.getToken().toString()));
        return localToken;
    }
}
