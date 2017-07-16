package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

/**
 * This fragment will allow the user to identify a bee species based on its head, thorax
 * and abdomen patterns. It will also let the user review the image they selected.
 */

public class IdentifyFragment extends Fragment {
    private static final String TAG = IdentifyFragment.class.toString();
    private enum BeePart {FACE, ABDOMEN, THORAX};

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.identify_fragment, container, false);

        TextView nextButton = (TextView) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ReviewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        final TextView backButton = (TextView) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        SubmissionActivity activity = (SubmissionActivity) getActivity();
        File imageFile = activity.getSubmission().getImageFile();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        ImageView preview = (ImageView) view.findViewById(R.id.previewImageView);
        preview.setImageBitmap(bitmap);

        final LinearLayout beePicker = (LinearLayout) view.findViewById(R.id.beePicker);
        setScrollView(beePicker, BeePart.FACE);

        TextView faceButton = (TextView) view.findViewById(R.id.faceButton);
        faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScrollView(beePicker, BeePart.FACE);
            }
        });

        TextView abdomenButton = (TextView) view.findViewById(R.id.abdomenButton);
        abdomenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScrollView(beePicker, BeePart.ABDOMEN);
            }
        });

        TextView thoraxButton = (TextView) view.findViewById(R.id.thoraxButton);
        thoraxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setScrollView(beePicker, BeePart.THORAX);
            }
        });

        return view;
    }

    private void setScrollView(LinearLayout scrollView, BeePart type) {
        scrollView.removeAllViews();
        switch(type) {
            case FACE:
                int[] faceAssets = {R.drawable.face_black, R.drawable.face_yellow};
                for (int i = 0; i < faceAssets.length; i++) {
                    ImageView view = new ImageView(getActivity());
                    view.setImageResource(faceAssets[i]);
                    scrollView.addView(view);
                }
                break;
            case ABDOMEN:
                int[] abdomenAssets = {R.drawable.ab_byb, R.drawable.ab_red_tail, R.drawable.ab_white_tail, R.drawable.ab_y_stripe, R.drawable.ab_yb, R.drawable.ab_yby, R.drawable.ab_yry, R.drawable.ab_yyy};
                for (int i = 0; i < abdomenAssets.length; i++) {
                    ImageView view = new ImageView(getActivity());
                    view.setImageResource(abdomenAssets[i]);
                    scrollView.addView(view);
                }
                break;
            case THORAX:
                int[] thoraxAssets = {R.drawable.thorax_bdot, R.drawable.thorax_whsh, R.drawable.thorax_ybb, R.drawable.thorax_yby, R.drawable.thorax_yyy};
                for (int i = 0; i < thoraxAssets.length; i++) {
                    ImageView view = new ImageView(getActivity());
                    view.setImageResource(thoraxAssets[i]);
                    scrollView.addView(view);
                }
                break;
        }
    }
}
