package com.blueprint.foe.beetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A generic dialog that should be modified for each type of dialog using the Bundle passed
 * into onCreateDialog.
 */
public class SpinningIconDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.spinning_icon_dialog, null);

        builder.setView(view);
        return builder.create();
    }
}

