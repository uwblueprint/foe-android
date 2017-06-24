package com.blueprint.foe.beetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    private CallbackManager callbackManager;
    private Callback loginCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // Other app specific specialization

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    Call<BeeTrackerCaller.SignupResponse> token = caller.signup(loginResult.getAccessToken());
                    token.enqueue(loginCallback);
                    //Log.v(TAG, token);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        loginCallback = new Callback<BeeTrackerCaller.SignupResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.SignupResponse> call, Response<BeeTrackerCaller.SignupResponse> response) {
                // Get result Repo from response.body()
                Log.d(TAG, "got response string: " + response.body().getToken());
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SignupResponse> call, Throwable t) {
                Log.e(TAG, "loginCallback ERROR" + t.toString());
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
