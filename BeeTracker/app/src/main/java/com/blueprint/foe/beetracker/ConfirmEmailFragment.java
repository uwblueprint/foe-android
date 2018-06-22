package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConfirmEmailFragment extends Fragment {
    private static final String TAG = SignUpFragment.class.toString();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_email_fragment, container, false);

        return view;
    }

}
