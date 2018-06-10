package com.blueprint.foe.beetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Exceptions.EmptyCredentialsException;
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
    private Callback emailPasswordLoginCallback;
    private Callback emailPasswordSignupCallback;
    private Callback facebookLoginCallback;
    private CallbackManager facebookCallbackManager;
    private SpinningIconDialog spinningIconDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button emailLoginButton = (Button) findViewById(R.id.email_login_button);
        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpinningIconDialog();

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
                    String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
                    Call<BeeTrackerCaller.EmailPasswordSigninResponse> call = caller.emailPasswordSignin(email, password);
                    call.enqueue(emailPasswordLoginCallback);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    showErrorDialog(getString(R.string.error_message_login));
                } catch (EmptyCredentialsException ece) {
                    Log.e(TAG, ece.toString());
                    ece.printStackTrace();
                }
            }
        });

        emailPasswordLoginCallback = new Callback<BeeTrackerCaller.EmailPasswordSigninResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.EmailPasswordSigninResponse> call, Response<BeeTrackerCaller.EmailPasswordSigninResponse> response) {
                if (response.code() == 401 || response.headers() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    LoginManager.getInstance().logOut();
                    spinningIconDialog.dismiss();
                    showErrorDialog(getString(R.string.error_message_login));
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().putString(getString(R.string.preference_login_token), response.headers().get("access-token")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_token_type), response.headers().get("token-type")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_client), response.headers().get("client")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_expiry), response.headers().get("expiry")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_uid), response.headers().get("uid")).commit();
                navigateToHome();
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.EmailPasswordSigninResponse> call, Throwable t) {
                handleLoginFailure("email password", t);
            }
        };

        TextView signUpButton = (TextView) findViewById(R.id.signup_with_email);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
                    String password = ((EditText) findViewById(R.id.password_input)).getText().toString();

                    Call<BeeTrackerCaller.EmailPasswordSignupResponse> call = caller.emailPasswordSignup(email, password);
                    call.enqueue(emailPasswordSignupCallback);

                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    sharedPref.edit().putString(getString(R.string.signup_email), email).commit();
                    sharedPref.edit().putString(getString(R.string.signup_password), password).commit();
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                } catch (EmptyCredentialsException ece) {
                    Log.e(TAG, ece.toString());
                    ece.printStackTrace();
                }
            }
        });

        emailPasswordSignupCallback = new Callback<BeeTrackerCaller.EmailPasswordSignupResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.EmailPasswordSignupResponse> call, Response<BeeTrackerCaller.EmailPasswordSignupResponse> response) {
                if (response.code() == 401 || response.body() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    LoginManager.getInstance().logOut();
                    return;
                }
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.EmailPasswordSignupResponse> call, Throwable t) {
                Log.e(TAG, "There was an error with the email password signup callback + " + t.toString());
                t.printStackTrace();
            }
        };

        TextView forgotPasswordButton = (TextView) findViewById(R.id.forgot_password);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing
            }
        });

        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginButton.setReadPermissions("email");
        // Other app specific specialization

        // Callback registration
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpinningIconDialog();
            }
        });
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    Call<BeeTrackerCaller.FacebookSigninResponse> token = caller.facebookSignup(loginResult.getAccessToken());
                    token.enqueue(facebookLoginCallback);
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

        facebookLoginCallback = new Callback<BeeTrackerCaller.FacebookSigninResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.FacebookSigninResponse> call, Response<BeeTrackerCaller.FacebookSigninResponse> response) {
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
            public void onFailure(Call<BeeTrackerCaller.FacebookSigninResponse> call, Throwable t) {
                handleLoginFailure("facebook", t);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void showSpinningIconDialog() {
        spinningIconDialog = new SpinningIconDialog();
        spinningIconDialog.show(getFragmentManager(), "SpinningPopup");
    }

    private void handleLoginFailure(String failureType, Throwable t) {
        Log.e(TAG, "There was an error with the " + failureType + " login callback + " + t.toString());
        t.printStackTrace();
        LoginManager.getInstance().logOut();
        spinningIconDialog.dismiss();
        showErrorDialog(getString(R.string.error_message_login));
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
