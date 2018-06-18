package com.blueprint.foe.beetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Exceptions.EmptyCredentialsException;
import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BeeAlertDialogListener {
    private static final String TAG = MainActivity.class.toString();
    private Callback emailPasswordLoginCallback;
    private Callback emailPasswordSignupCallback;
    private SpinningIconDialog spinningIconDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText emailInput = (EditText) findViewById(R.id.email_input);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetInputFieldActiveState(emailInput);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText passwordInput = (EditText) findViewById(R.id.password_input);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetInputFieldActiveState(passwordInput);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button emailLoginButton = (Button) findViewById(R.id.email_login_button);
        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");

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
                    showErrorDialog(getString(R.string.error_message_empty_credentials));
                    setInputFieldsActiveStateToError(emailInput, passwordInput);
                }
            }
        });

        emailPasswordLoginCallback = new Callback<BeeTrackerCaller.EmailPasswordSigninResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.EmailPasswordSigninResponse> call, Response<BeeTrackerCaller.EmailPasswordSigninResponse> response) {
                if (response.code() == 401 || response.headers() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    showErrorDialog(getString(R.string.error_message_login));
                    setInputFieldsActiveStateToError(emailInput, passwordInput);
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
                Log.e(TAG, "There was an error with the login callback + " + t.toString());
                t.printStackTrace();
                showErrorDialog(getString(R.string.error_message_login));
            }
        };

        TextView signUpButton = (TextView) findViewById(R.id.signup_with_email);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");

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
                    showErrorDialog(getString(R.string.error_message_signup));
                } catch (EmptyCredentialsException ece) {
                    Log.e(TAG, ece.toString());
                    ece.printStackTrace();
                    showErrorDialog(getString(R.string.error_message_empty_credentials));
                    setInputFieldsActiveStateToError(emailInput, passwordInput);
                }
            }
        });

        emailPasswordSignupCallback = new Callback<BeeTrackerCaller.EmailPasswordSignupResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.EmailPasswordSignupResponse> call, Response<BeeTrackerCaller.EmailPasswordSignupResponse> response) {
                if (response.code() == 401 || response.body() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    showErrorDialog(getString(R.string.error_message_signup));
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
    }

    private void navigateToHome() {
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorDialog(String message) {
        if (spinningIconDialog != null) {
            spinningIconDialog.dismiss();
        }
        BeeAlertErrorDialog dialog = new BeeAlertErrorDialog();
        Bundle args = new Bundle();
        args.putString(BeeAlertErrorDialog.ERROR_MESSAGE_KEY, message);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "ErrorMessage");
    }

    private void resetInputFieldActiveState(EditText editText) {
        ViewCompat.setBackgroundTintList(editText, ColorStateList.valueOf(getResources().getColor(R.color.grassGreen)));
    }

    private void setInputFieldsActiveStateToError(EditText emailInput, EditText passwordInput) {
        ViewCompat.setBackgroundTintList(emailInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(passwordInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
    }

    @Override
    public void onDialogFinishClick(int id) {}
}
