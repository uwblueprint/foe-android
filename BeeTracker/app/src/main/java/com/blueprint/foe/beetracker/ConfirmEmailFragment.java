package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConfirmEmailFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.toString();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_email_fragment, container, false);

        TextView almostThere = (TextView) view.findViewById(R.id.almostThereText);
        TextView confirmEmailDescription = (TextView) view.findViewById(R.id.confirmEmailDescription);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String email = sharedPref.getString(getString(R.string.signup_email), null);
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

        return view;
    }

}
