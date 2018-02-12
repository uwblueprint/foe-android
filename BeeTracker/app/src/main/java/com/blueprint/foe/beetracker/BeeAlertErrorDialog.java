package com.blueprint.foe.beetracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;

/**
 * A generic dialog that should be modified for each type of dialog using the Bundle passed into
 * onCreateDialog.
 */
public class BeeAlertErrorDialog extends DialogFragment {
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    BeeAlertDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (BeeAlertDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement BeeAlertDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_error, null);

        Bundle b = getArguments();
        String providedErrorMessage = b.getString(ERROR_MESSAGE);

        TextView tvErrorMessage = (TextView) view.findViewById(R.id.errorMessage);
        tvErrorMessage.setText(providedErrorMessage);

        TextView tvDoneButton = (TextView) view.findViewById(R.id.close);
        tvDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogFinishClick();
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}
