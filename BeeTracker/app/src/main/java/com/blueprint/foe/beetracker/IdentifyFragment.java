package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Model.BeePart;
import com.blueprint.foe.beetracker.Model.PartsPickerAdapter;
import com.blueprint.foe.beetracker.Model.Submission;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment will allow the user to identify a bee species based on its head, thorax
 * and abdomen patterns. It will also let the user review the image they selected.
 */
public class IdentifyFragment extends Fragment implements OnBeePartSelectedListener {
    private static final String TAG = IdentifyFragment.class.toString();
    private PartsPickerAdapter mFaceAdapter;
    private PartsPickerAdapter mAbdomenAdapter;
    private PartsPickerAdapter mThoraxAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mFaceButton;
    private TextView mAbdomenButton;
    private TextView mThoraxButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.identify_fragment, container, false);

        TextView nextButton = (TextView) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "clicked");
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

        Submission submission = ((SubmissionInterface) getActivity()).getSubmission();
        Bitmap bitmap = BitmapFactory.decodeFile(submission.getImageFilePath());
        int scaledWidth = container.getWidth();
        int scaledHeight = (int)(((double)bitmap.getHeight() / (double)bitmap.getWidth()) * container.getWidth());
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
        ImageView preview = (ImageView) view.findViewById(R.id.previewImageView);
        preview.setImageBitmap(scaled);

        mFaceButton = (TextView) view.findViewById(R.id.faceButton);
        mFaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setAdapter(mFaceAdapter);
            }
        });

        mAbdomenButton = (TextView) view.findViewById(R.id.abdomenButton);
        mAbdomenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setAdapter(mAbdomenAdapter);
            }
        });

        mThoraxButton = (TextView) view.findViewById(R.id.thoraxButton);
        mThoraxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setAdapter(mThoraxAdapter);
            }
        });

        createAdapters(submission);
        onBeePartSelected();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mFaceAdapter);

        return view;
    }

    private void createAdapters(Submission submission) {
        int[] faceAssets = {R.drawable.face_black, R.drawable.face_yellow};
        int[] abdomenAssets = {R.drawable.ab_byb, R.drawable.ab_red_tail, R.drawable.ab_white_tail, R.drawable.ab_y_stripe, R.drawable.ab_yb, R.drawable.ab_yby, R.drawable.ab_yry, R.drawable.ab_yyy};
        int[] thoraxAssets = {R.drawable.thorax_bdot, R.drawable.thorax_whsh, R.drawable.thorax_ybb, R.drawable.thorax_yby, R.drawable.thorax_yyy};

        List<BeePart> faces = new ArrayList<>();
        for (int i = 0; i < faceAssets.length; i++) {
            faces.add(new BeePart(i, faceAssets[i], getActivity()));
        }
        List<BeePart> abdomens = new ArrayList<>();
        for (int i = 0; i < abdomenAssets.length; i++) {
            abdomens.add(new BeePart(i, abdomenAssets[i], getActivity()));
        }
        List<BeePart> thoraxes = new ArrayList<>();
        for (int i = 0; i < thoraxAssets.length; i++) {
            thoraxes.add(new BeePart(i, thoraxAssets[i], getActivity()));
        }

        if (submission.getFace() > -1) {
            faces.get(submission.getFace()).setSelection(true);
        }
        if (submission.getAbdomen() > -1) {
            abdomens.get(submission.getAbdomen()).setSelection(true);
        }
        if (submission.getThorax() > -1) {
            thoraxes.get(submission.getThorax()).setSelection(true);
        }

        mFaceAdapter = new PartsPickerAdapter(faces, BeePart.BeePartType.Face, this);
        mThoraxAdapter = new PartsPickerAdapter(thoraxes, BeePart.BeePartType.Thorax, this);
        mAbdomenAdapter = new PartsPickerAdapter(abdomens, BeePart.BeePartType.Abdomen, this);
    }

    private void errorAndExit(String message) {
        getActivity().finish();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBeePartSelected() {
        Submission submission = ((SubmissionInterface) getActivity()).getSubmission();
        if (submission.getFace() > -1) {
            mFaceButton.setTextColor(getResources().getColor(R.color.grassGreen));
        }
        if (submission.getAbdomen() > -1) {
            mAbdomenButton.setTextColor(getResources().getColor(R.color.grassGreen));
        }
        if (submission.getThorax() > -1) {
            mThoraxButton.setTextColor(getResources().getColor(R.color.grassGreen));
        }
    }
}
