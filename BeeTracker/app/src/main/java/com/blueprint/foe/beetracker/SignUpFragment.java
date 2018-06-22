package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Exceptions.EmptyCredentialsException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.toString();
    private Callback emailPasswordSignupCallback;
    private SpinningIconDialog spinningIconDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sign_up_fragment, container, false);

        final EditText nameInput = (EditText) view.findViewById(R.id.name_input);
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetInputFieldActiveState(nameInput);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText emailInput = (EditText) view.findViewById(R.id.email_input);
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

        final EditText passwordInput = (EditText) view.findViewById(R.id.password_input);
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

        final EditText confirmPasswordInput = (EditText) view.findViewById(R.id.confirm_password_input);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetInputFieldActiveState(confirmPasswordInput);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String signupEmail = sharedPref.getString(getString(R.string.signup_email), null);
        String signupPassword = sharedPref.getString(getString(R.string.signup_password), null);

        if (signupEmail != null) {
            emailInput.setText(signupEmail);
        }
        if (signupPassword != null) {
            passwordInput.setText(signupPassword);
        }

        ImageButton cancelButton = (ImageButton) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to main activity
                getFragmentManager().popBackStack();
            }
        });

        Button signupButton = (Button) view.findViewById(R.id.sign_up_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    String name = ((EditText) view.findViewById(R.id.name_input)).getText().toString();
                    String email = ((EditText) view.findViewById(R.id.email_input)).getText().toString();
                    String password = ((EditText) view.findViewById(R.id.password_input)).getText().toString();
                    String confirmPassword = ((EditText) view.findViewById(R.id.confirm_password_input)).getText().toString();

                    if (!password.equals(confirmPassword)) {
                        showErrorDialog(getString(R.string.error_message_signup));
                        return;
                    }

                    Call<BeeTrackerCaller.EmailPasswordSignupResponse> call = caller.emailPasswordSignup(name, email, password);
                    call.enqueue(emailPasswordSignupCallback);

                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    sharedPref.edit().putString(getString(R.string.signup_name), name).commit();
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
                    setInputFieldsActiveStateToError(nameInput, emailInput, passwordInput, confirmPasswordInput);
                }

                Fragment confirmEmailFragment = new ConfirmEmailFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.constraintLayout, confirmEmailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

        return view;
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

    private void setInputFieldsActiveStateToError(EditText nameInput, EditText emailInput, EditText passwordInput, EditText confirmPasswordInput) {
        ViewCompat.setBackgroundTintList(nameInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(emailInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(passwordInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(confirmPasswordInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
    }

}
