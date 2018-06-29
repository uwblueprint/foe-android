package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.toString();
    private static final int MINIMUM_PASSWORD_LENGTH = 8;
    private TextView mErrorMessage;
    private TextView mNameLabel;
    private TextView mEmailLabel;
    private TextView mPasswordLabel;
    private TextView mConfirmPasswordLabel;
    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPasswordInput;
    private EditText mConfirmPasswordInput;
    private Callback signupCallback;
    private SpinningIconDialog spinningIconDialog;

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        mErrorMessage = (TextView) view.findViewById(R.id.error_message);

        mNameLabel = (TextView) view.findViewById(R.id.sign_up_name_label);
        mEmailLabel = (TextView) view.findViewById(R.id.sign_up_email_label);
        mPasswordLabel = (TextView) view.findViewById(R.id.sign_up_password_label);
        mConfirmPasswordLabel = (TextView) view.findViewById(R.id.sign_up_confirm_password_label);
        mNameInput = (EditText) view.findViewById(R.id.sign_up_name_input);
        mEmailInput = (EditText) view.findViewById(R.id.sign_up_email_input);
        mPasswordInput = (EditText) view.findViewById(R.id.sign_up_password_input);
        mConfirmPasswordInput = (EditText) view.findViewById(R.id.sign_up_confirm_password_input);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String signupEmail = sharedPref.getString(getString(R.string.signup_email), null);
        String signupPassword = sharedPref.getString(getString(R.string.signup_password), null);

        if (signupEmail != null) {
            mEmailInput.setText(signupEmail);
        }
        if (signupPassword != null) {
            mPasswordInput.setText(signupPassword);
        }

        mNameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLabelAndFieldToActiveState(mNameLabel);
                } else {
                    setLabelAndFieldToInactiveState(mNameLabel);
                }
            }
        });

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

        mConfirmPasswordInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLabelAndFieldToActiveState(mConfirmPasswordLabel);
                } else {
                    setLabelAndFieldToInactiveState(mConfirmPasswordLabel);
                }
            }
        });

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
                hideErrorMessage();
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    String name = ((EditText) view.findViewById(R.id.sign_up_name_input)).getText().toString();
                    String email = ((EditText) view.findViewById(R.id.sign_up_email_input)).getText().toString();
                    String password = ((EditText) view.findViewById(R.id.sign_up_password_input)).getText().toString();
                    String confirmPassword = ((EditText) view.findViewById(R.id.sign_up_confirm_password_input)).getText().toString();

                    if (!signupFieldsAreValid(name, email, password, confirmPassword)) {
                        return;
                    }

                    SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    sharedPref.edit().putString(getString(R.string.signup_name), name).commit();
                    sharedPref.edit().putString(getString(R.string.signup_email), email).commit();
                    sharedPref.edit().putString(getString(R.string.signup_password), password).commit();

                    Call<BeeTrackerCaller.SignUpResponse> call = caller.signUp(name, email, password);
                    call.enqueue(signupCallback);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    showErrorDialog(getString(R.string.error_message_signup));
                }
            }
        });

        signupCallback = new Callback<BeeTrackerCaller.SignUpResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.SignUpResponse> call, Response<BeeTrackerCaller.SignUpResponse> response) {
                if (response.code() == 401 || response.body() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    showErrorDialog(getString(R.string.error_message_signup));
                    setAllLabelsAndFieldsToError();
                    return;
                }

                if (spinningIconDialog != null) {
                    spinningIconDialog.dismiss();
                }

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                ConfirmEmailFragment confirmEmailFragment = new ConfirmEmailFragment();
                fragmentTransaction.replace(R.id.constraintLayout, confirmEmailFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SignUpResponse> call, Throwable t) {
                Log.e(TAG, "There was an error with the signup callback + " + t.toString());
                t.printStackTrace();
                showErrorDialog(getString(R.string.error_message_signup));
                setAllLabelsAndFieldsToError();
            }
        };

        return view;
    }

    private void setLabelAndFieldToActiveState(TextView label) {
        label.setTextColor(getResources().getColor(R.color.grassGreen));

        EditText inputField = (EditText) getView().findViewById(label.getLabelFor());
        ViewCompat.setBackgroundTintList(inputField, ColorStateList.valueOf(getResources().getColor(R.color.grassGreen)));
    }

    private void setLabelAndFieldToInactiveState(TextView label) {
        label.setTextColor(getResources().getColor(R.color.subheadingTextColour));

        EditText inputField = (EditText) getView().findViewById(label.getLabelFor());
        ViewCompat.setBackgroundTintList(inputField, ColorStateList.valueOf(getResources().getColor(R.color.mediumGrey)));
    }

    private void setPasswordFieldsToError() {
        ArrayList<TextView> labelsToSet = new ArrayList<>();
        labelsToSet.add(mPasswordLabel);
        labelsToSet.add(mConfirmPasswordLabel);
        setLabelsAndFieldsToError(labelsToSet);
    }

    private void setLabelsAndFieldsToError(ArrayList<TextView> labels) {
        for (TextView label: labels) {
            label.setTextColor(getResources().getColor(R.color.poppyRed));

            EditText inputFieldToSet;
            switch (label.getLabelFor()) {
                case R.id.sign_up_name_input:
                    inputFieldToSet = mNameInput;
                    break;
                case R.id.sign_up_email_input:
                    inputFieldToSet = mEmailInput;
                    break;
                case R.id.sign_up_password_input:
                    inputFieldToSet = mPasswordInput;
                    break;
                case R.id.sign_up_confirm_password_input:
                    inputFieldToSet = mConfirmPasswordInput;
                    break;
                default:
                    inputFieldToSet = mEmailInput;
            }
            ViewCompat.setBackgroundTintList(inputFieldToSet, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        }
    }
    private void setAllLabelsAndFieldsToError() {
        mNameLabel.setTextColor(getResources().getColor(R.color.poppyRed));
        mEmailLabel.setTextColor(getResources().getColor(R.color.poppyRed));
        mPasswordLabel.setTextColor(getResources().getColor(R.color.poppyRed));
        mConfirmPasswordLabel.setTextColor(getResources().getColor(R.color.poppyRed));
        ViewCompat.setBackgroundTintList(mNameInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(mEmailInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(mPasswordInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
        ViewCompat.setBackgroundTintList(mConfirmPasswordInput, ColorStateList.valueOf(getResources().getColor(R.color.poppyRed)));
    }


    private boolean signupFieldsAreValid(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage(getString(R.string.error_message_empty_signup_credentials));
            ArrayList<TextView> labelsToSet = new ArrayList<>();
            if (name.isEmpty()) {
                labelsToSet.add(mNameLabel);
            }
            if (email.isEmpty()) {
                labelsToSet.add(mEmailLabel);
            }
            if (password.isEmpty()) {
                labelsToSet.add(mPasswordLabel);
            }
            if (confirmPassword.isEmpty()) {
                labelsToSet.add(mConfirmPasswordLabel);
            }
            setLabelsAndFieldsToError(labelsToSet);
            return false;
        } else if (!password.equals(confirmPassword)) {
            showErrorMessage(getString(R.string.error_message_password_mismatch));
            setPasswordFieldsToError();
            return false;
        } else if (password.length() < MINIMUM_PASSWORD_LENGTH) {
            showErrorMessage(getString(R.string.error_message_password_too_short));
            setPasswordFieldsToError();
            return false;
        }

        return true;
    }

    private void showErrorMessage(String message) {
        if (spinningIconDialog != null) {
            spinningIconDialog.dismiss();
        }
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(getString(R.string.signup_error_message_placeholder, message));
    }

    private void hideErrorMessage() {
        if (mErrorMessage.getVisibility() == View.VISIBLE) {
            mErrorMessage.setVisibility(View.GONE);
        }
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

}
