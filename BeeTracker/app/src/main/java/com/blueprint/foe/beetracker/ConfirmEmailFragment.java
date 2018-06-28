package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmEmailFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.toString();
    private Callback resendCallback;
    private SpinningIconDialog spinningIconDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_email_fragment, container, false);

        TextView almostThere = (TextView) view.findViewById(R.id.almostThereText);
        TextView confirmEmailDescription = (TextView) view.findViewById(R.id.confirmEmailDescription);
        TextView resendEmail = (TextView) view.findViewById(R.id.resendEmail);
        TextView backToLogin = (TextView) view.findViewById(R.id.backToLogin);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String email = sharedPref.getString(getString(R.string.signup_email), null);
        String fullName = sharedPref.getString(getString(R.string.signup_name), null);

        if (fullName != null && !fullName.isEmpty()) {
            String[] nameArray = fullName.split(" ");
            almostThere.setText(getString(R.string.almost_there, nameArray[0]));
        }
        if (email != null) {
            confirmEmailDescription.setText(getString(R.string.confirm_email_description, email));
        }

        ImageButton cancelButton = (ImageButton) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to main activity
                getFragmentManager().popBackStack("main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        resendCallback = new Callback<BeeTrackerCaller.ResendEmailResponse>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.ResendEmailResponse> call, Response<BeeTrackerCaller.ResendEmailResponse> response) {
                if (response.code() == 401 || response.body() == null) {
                    Log.e(TAG, "The response from the server is 401 + " + response.message());
                    showErrorDialog(getString(R.string.error_message_resend_email));
                    return;
                }

                if (spinningIconDialog != null) {
                    spinningIconDialog.dismiss();
                }

                Toast.makeText(getActivity(), getString(R.string.confirm_email_resent, email), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.ResendEmailResponse> call, Throwable t) {
                Log.e(TAG, "There was an error with the signup callback + " + t.toString());
                t.printStackTrace();
                showErrorDialog(getString(R.string.error_message_resend_email));
            }
        };

        resendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinningIconDialog = new SpinningIconDialog();
                spinningIconDialog.show(getFragmentManager(), "SpinningPopup");

                BeeTrackerCaller caller = new BeeTrackerCaller();
                try {
                    Call<BeeTrackerCaller.ResendEmailResponse> call = caller.resendEmail(email);
                    call.enqueue(resendCallback);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    showErrorDialog(getString(R.string.error_message_signup));
                }
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Back to main activity
                getFragmentManager().popBackStack("main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

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
}
