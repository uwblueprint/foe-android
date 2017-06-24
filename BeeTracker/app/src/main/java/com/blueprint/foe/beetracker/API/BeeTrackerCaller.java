package com.blueprint.foe.beetracker.API;

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
    public static final String API_URL = "https://foe-api.herokuapp.com/";
    public Call<SignupResponse> signup(AccessToken token) throws IOException{
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BeeTrackerService service = retrofit.create(BeeTrackerService.class);
        return service.facebookAuth(new SignupRequest(token.getToken().toString()));
    }
}
