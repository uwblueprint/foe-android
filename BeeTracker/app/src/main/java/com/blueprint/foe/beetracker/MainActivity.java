package com.blueprint.foe.beetracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.blueprint.foe.beetracker.Model.Submission;
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
                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    Call<BeeTrackerCaller.SignupResponse> token = caller.signup(loginResult.getAccessToken());
                    token.enqueue(loginCallback);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    // TODO: https://github.com/uwblueprint/foe/issues/16
                }
            }

            @Override
            public void onCancel() {
                // TODO: https://github.com/uwblueprint/foe/issues/16
            }

            @Override
            public void onError(FacebookException exception) {
                // TODO: https://github.com/uwblueprint/foe/issues/16
            }
        });


        loginCallback = new Callback<BeeTrackerCaller.SignupResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.SignupResponse> call, Response<BeeTrackerCaller.SignupResponse> response) {
                Log.d(TAG, "got response string: " + response.body().getToken());
                // TODO: Store returned token https://github.com/uwblueprint/foe/issues/17
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SignupResponse> call, Throwable t) {
                Log.e(TAG, "loginCallback ERROR" + t.toString());
                // TODO: https://github.com/uwblueprint/foe/issues/16
            }
        };

        Button submitButton = (Button) findViewById(R.id.submit);
        final Context context = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submission submission = new Submission();
                submission.setFace(0);
                submission.setAbdomen(0);
                submission.setHabitat(Submission.Habitat.HouseGarden);
                Location location = new Location("TEST");
                location.setLatitude(-23.532);
                location.setLongitude(78.23);
                submission.setLocation(location);
                StorageAccessor storageAccessor = new StorageAccessor();
                try {
                    storageAccessor.store(context, submission);
                    System.out.println("stored");
                } catch (IOException e) {
                    System.out.println(e.toString());
                }

            }
        });

        Button loadButton = (Button) findViewById(R.id.load);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageAccessor storageAccessor = new StorageAccessor();
                try {
                    Submission submission = storageAccessor.load(context);
                    submission.print();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
