package com.blueprint.foe.beetracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;

/**
 * A generic dialog that should be modified for each type of dialog using the Bundle passed
 * into onCreateDialog.
 */
public class BeeAlertDialog extends DialogFragment {
    public static final String IMAGE_SRC = "IMAGE_SRC";
    public static final String HEADING = "HEADING";
    public static final String PARAGRAPH = "PARAGRAPH";
    public static final String FINISH = "FINISH";
    BeeAlertDialogListener mListener;

    // Consider changing all fragments to v4 support so that we don't need the duplicate onAttach
    // methods. onAttack(context) only became available in API 23
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BeeAlertDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement BeeAlertDialogListener");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BeeAlertDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement BeeAlertDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_general, null);

        Bundle b = getArguments();
        int providedImageDrawable = b.getInt(IMAGE_SRC);
        String providedHeading = b.getString(HEADING);
        String providedParagraph = b.getString(PARAGRAPH);
        String providedFinishText = b.getString(FINISH, "");

        ImageView ivImage = (ImageView) view.findViewById(R.id.dialog_image);
        ivImage.setImageResource(providedImageDrawable);

        TextView tvHeading = (TextView) view.findViewById(R.id.heading);
        tvHeading.setText(providedHeading);

        TextView tvParagraph = (TextView) view.findViewById(R.id.paragraph);
        tvParagraph.setText(providedParagraph);

        TextView tvDoneButton = (TextView) view.findViewById(R.id.finish);
        if (!providedFinishText.isEmpty()) {
            tvDoneButton.setText(providedFinishText);
        }
        tvDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogFinishClick(BeeAlertDialogListener.NORMAL_DIALOG);
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}
