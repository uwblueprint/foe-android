package com.blueprint.foe.beetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BeeAlertDialogListener {
    private static final String TAG = MainActivity.class.toString();
    private CallbackManager callbackManager;
    private Callback loginCallback;
    private SpinningIconDialog spinningIconDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView skipButton = (TextView) findViewById(R.id.skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().putBoolean(getString(R.string.preference_logged_in_as_guest), true).commit();
                navigateToHome();
            }
        });

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // other app specific specialization

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    Call<BeeTrackerCaller.SignupResponse> token = caller.signup(loginResult.getAccessToken());
                    token.enqueue(loginCallback);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    showErrorDialog(getString(R.string.error_message_login));
                }
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "There was an error on Facebook login. " + exception.toString());
                exception.printStackTrace();
                showErrorDialog(getString(R.string.error_message_login));
            }
        });


        loginCallback = new Callback<BeeTrackerCaller.SignupResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.SignupResponse> call, Response<BeeTrackerCaller.SignupResponse> response) {
                if (response.code() == 401 || response.body() == null ||  response.body().getToken() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    LoginManager.getInstance().logOut();
                    spinningIconDialog.dismiss();
                    showErrorDialog(getString(R.string.error_message_login));
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().putString(getString(R.string.preference_login_token), response.body().getToken()).commit();
                navigateToHome();
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SignupResponse> call, Throwable t) {
                Log.e(TAG, "There was an error with the loginCallback + " + t.toString());
                t.printStackTrace();
                LoginManager.getInstance().logOut();
                spinningIconDialog.dismiss();
                showErrorDialog(getString(R.string.error_message_login));
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void navigateToHome() {
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorDialog(String message) {
        BeeAlertErrorDialog dialog = new BeeAlertErrorDialog();
        Bundle args = new Bundle();
        args.putString(BeeAlertErrorDialog.ERROR_MESSAGE_KEY, message);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "ErrorMessage");
    }

    @Override
    public void onDialogFinishClick(int id) {}
}
