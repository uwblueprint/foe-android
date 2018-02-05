package com.blueprint.foe.beetracker;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This fragment will allow the user to review the species, location, as well as add the
 * environment type and current weather.
 */

public class ReviewFragment extends Fragment implements BeeAlertDialog.BeeAlertDialogListener {
    private static final String TAG = ReviewFragment.class.toString();

    @Override
    public void onDialogFinishClick() {
        // User touched the dialog's positive button
        getActivity().finish();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);

        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        final ReviewFragment fragment = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeeAlertDialog dialog = new BeeAlertDialog();
                Bundle args = new Bundle();
                args.putInt(BeeAlertDialog.IMAGE_SRC, R.drawable.bee_image_popup);
                args.putString(BeeAlertDialog.HEADING, getString(R.string.submit_dialog_heading));
                args.putString(BeeAlertDialog.PARAGRAPH, getString(R.string.submit_dialog_paragraph));
                dialog.setArguments(args);
                dialog.setListener(fragment);
                dialog.show(getActivity().getFragmentManager(), "SubmissionFragment");
            }
        });

        Button backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
