package com.blueprint.foe.beetracker;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BeeAlertDialogListener {
    private static final String TAG = MainActivity.class.toString();
    private TextView mEmailLabel;
    private TextView mPasswordLabel;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private Callback emailPasswordLoginCallback;
    private SpinningIconDialog spinningIconDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailLabel = (TextView) findViewById(R.id.email_label);
        mPasswordLabel = (TextView) findViewById(R.id.password_label);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mPasswordInput = (EditText) findViewById(R.id.password_input);

        mEmailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLabelAndFieldToActiveState(mEmailLabel);
                } else {
                    setLabelAndFieldToInactiveState(mEmailLabel);
                }
            }
        });

        mPasswordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLabelAndFieldToActiveState(mPasswordLabel);
                } else {
                    setLabelAndFieldToInactiveState(mPasswordLabel);
                }
            }
        });

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String email = sharedPref.getString(getString(R.string.signup_email), null);
        String password = sharedPref.getString(getString(R.string.signup_password), null);

        if (email != null) {
            mEmailInput.setText(email);
        }
        if (password != null) {
            mPasswordInput.setText(password);
        }


        Button emailLoginButton = (Button) findViewById(R.id.email_login_button);
        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    String email = mEmailInput.getText().toString();
                    String password = mPasswordInput.getText().toString();

                    if (!loginFieldsAreValid(email, password)) {
                        return;
                    }

                    Call<BeeTrackerCaller.LogInResponse> call = caller.logIn(email, password);
                    call.enqueue(emailPasswordLoginCallback);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    showErrorDialog(getString(R.string.error_message_login));
                }
            }
        });

        emailPasswordLoginCallback = new Callback<BeeTrackerCaller.LogInResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.LogInResponse> call, Response<BeeTrackerCaller.LogInResponse> response) {
                if (response.code() == 401 || response.headers() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        BeeTrackerCaller.LogInResponse message = gson.fromJson(response.errorBody().charStream(),BeeTrackerCaller.LogInResponse.class);
                        if (message != null && message.getConfirmationError() != null) {
                            showErrorDialog(message.getConfirmationError());
                            return;
                        }
                    }
                    showErrorDialog(getString(R.string.error_message_login));
                    setAllLabelsAndFieldsToError();

                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().putString(getString(R.string.preference_login_token), response.headers().get("access-token")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_token_type), response.headers().get("token-type")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_client), response.headers().get("client")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_expiry), response.headers().get("expiry")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_uid), response.headers().get("uid")).commit();

                sharedPref.edit().remove(getString(R.string.signup_name)).commit();
                sharedPref.edit().remove(getString(R.string.signup_email)).commit();
                sharedPref.edit().remove(getString(R.string.signup_password)).commit();
                navigateToHome();
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.LogInResponse> call, Throwable t) {
                Log.e(TAG, "There was an error with the login callback + " + t.toString());
                t.printStackTrace();
                showErrorDialog(getString(R.string.error_message_login));
                setAllLabelsAndFieldsToError();
            }
        };

        TextView signUpButton = (TextView) findViewById(R.id.signup_with_email);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailInput.getText().toString();
                String password = mPasswordInput.getText().toString();

                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().putString(getString(R.string.signup_email), email).commit();
                sharedPref.edit().putString(getString(R.string.signup_password), password).commit();

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                SignUpFragment signUpFragment = new SignUpFragment();
                fragmentTransaction.replace(R.id.constraintLayout, signUpFragment);
                fragmentTransaction.addToBackStack("main");
                fragmentTransaction.commit();
            }
        });

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

    private void setLabelAndFieldToActiveState(TextView label) {
        label.setTextColor(getResources().getColor(R.color.grassGreen));

        EditText inputField = (EditText) findViewById(label.getLabelFor());
        ViewCompat.setBackgroundTintList(inputField, ColorStateList.valueOf(getResources().getColor(R.color.grassGreen)));
    }

    private void setLabelAndFieldToInactiveState(TextView label) {
        label.setTextColor(getResources().getColor(R.color.subheadingTextColour));

        EditText inputField = (EditText) findViewById(label.getLabelFor());
        ViewCompat.setBackgroundTintList(inputField, ColorStateList.valueOf(getResources().getColor(R.color.mediumGrey)));
    }

    private void setLabelsAndFieldsToError(ArrayList<TextView> labels) {
        for (TextView label: labels) {
            label.setTextColor(getResources().getColor(R.color.poppyRed));

            EditText inputFieldToSet;
            switch (label.getLabelFor()) {
                case R.id.email_input:
                    inputFieldToSet = mEmailInput;
                    break;
                case R.id.password_input:
                    inputFieldToSet = mPasswordInput;
                    break;
                default:
                    inputFieldToSet = mEmailInput;
            }
            ViewCompat.setBackgroundTintList(inputFieldToSet, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        }
    }

    private void setAllLabelsAndFieldsToError() {
        mEmailLabel.setTextColor(getResources().getColor(R.color.poppyRed));
        mPasswordLabel.setTextColor(getResources().getColor(R.color.poppyRed));
        ViewCompat.setBackgroundTintList(mEmailInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(mPasswordInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
    }

    private boolean loginFieldsAreValid(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog(getString(R.string.error_message_empty_login_credentials));
            ArrayList<TextView> labelsToSet = new ArrayList<>();
            if (email.isEmpty()) {
                labelsToSet.add(mEmailLabel);
            }
            if (password.isEmpty()) {
                labelsToSet.add(mPasswordLabel);
            }
            setLabelsAndFieldsToError(labelsToSet);
            return false;
        }

        return true;
    }

    @Override
    public void onDialogFinishClick(int id) {}
}
