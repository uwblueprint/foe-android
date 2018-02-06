package com.blueprint.foe.beetracker;

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

/**
 * A generic dialog that should be modified for each type of dialog using the Bundle passed into
 * onCreateDialog.
 */
public class BeeAlertDialog extends DialogFragment {
    public static final String IMAGE_SRC = "IMAGE_SRC";
    public static final String HEADING = "HEADING";
    public static final String PARAGRAPH = "PARAGRAPH";
    BeeAlertDialogListener mListener;

    public interface BeeAlertDialogListener {
        void onDialogFinishClick();
    }

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
        View view = inflater.inflate(R.layout.dialog_general, null);

        Bundle b = getArguments();
        int providedImageDrawable = b.getInt(IMAGE_SRC);
        String providedHeading = b.getString(HEADING);
        String providedParagraph = b.getString(PARAGRAPH);

        ImageView ivImage = (ImageView) view.findViewById(R.id.dialog_image);
        ivImage.setImageResource(providedImageDrawable);

        TextView tvHeading = (TextView) view.findViewById(R.id.heading);
        tvHeading.setText(providedHeading);

        TextView tvParagraph = (TextView) view.findViewById(R.id.paragraph);
        tvParagraph.setText(providedParagraph);

        TextView tvDoneButton = (TextView) view.findViewById(R.id.finish);
        tvDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogFinishClick();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}
