package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;
import com.blueprint.foe.beetracker.Model.PartsPickerAdapter;
import com.blueprint.foe.beetracker.Model.Submission;

import static com.blueprint.foe.beetracker.Model.Submission.Species;

import java.util.Arrays;
import java.util.List;

/**
 * This fragment will allow the user to identify a bee species based on its head, thorax
 * and abdomen patterns. It will also let the user review the image they selected.
 */
public class IdentifyFragment extends Fragment implements BeeAlertDialogListener {
    private static final String TAG = IdentifyFragment.class.toString();
    private PartsPickerAdapter mEasternAdapter;
    private PartsPickerAdapter mWesternAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mEasternButton;
    private TextView mWesternButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.identify_fragment, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean(getString(R.string.preference_first_time_identifying), true);
        if (firstTime) {
            // Launch popup to explain to user how to identify bee parts
            BeeAlertDialog dialog = new BeeAlertDialog();
            dialog.setTargetFragment(this, 1);
            Bundle args = new Bundle();
            args.putInt(BeeAlertDialog.IMAGE_SRC, R.mipmap.picker_illustration);
            args.putString(BeeAlertDialog.HEADING, getString(R.string.identify_popup_heading));
            args.putString(BeeAlertDialog.PARAGRAPH, getString(R.string.identify_popup_message));
            args.putString(BeeAlertDialog.FINISH, getString(R.string.identify_popup_finish));
            dialog.setArguments(args);
            dialog.show(getActivity().getFragmentManager(), "IdentifyPopup");
            // Set boolean to false so they don't see this modal multiple times
            sharedPref.edit().putBoolean(getString(R.string.preference_first_time_identifying), false).commit();
        }

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

        CurrentSubmission submission = ((SubmissionInterface) getActivity()).getSubmission();
        Bitmap bitmap = submission.getBitmap();
        int scaledWidth = container.getWidth();
        int scaledHeight = (int)(((double)bitmap.getHeight() / (double)bitmap.getWidth()) * container.getWidth());
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
        ImageView preview = (ImageView) view.findViewById(R.id.previewImageView);
        preview.setImageBitmap(scaled);

        mEasternButton = (TextView) view.findViewById(R.id.easternButton);
        mEasternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setAdapter(mEasternAdapter);
                mEasternButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.grassGreen));
                mWesternButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.subheadingTextColour));
            }
        });

        mWesternButton = (TextView) view.findViewById(R.id.westernButton);
        mWesternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.setAdapter(mWesternAdapter);
                mWesternButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.grassGreen));
                mEasternButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.subheadingTextColour));
            }
        });

        createAdapters(submission);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mEasternAdapter);
        mEasternButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.grassGreen));

        CardView skipStepButton = (CardView) view.findViewById(R.id.skipStepButton);
        skipStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmissionInterface activity = (SubmissionInterface) getActivity();
                Submission submission = activity.getSubmission();
                submission.setSpecies(null);
                submission.setSpeciesType(null);

                Fragment newFragment = new ReviewFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

    private void createAdapters(CurrentSubmission submission) {
        List<Species> easternSpecies = Arrays.asList(
                Species.impatiens, Species.ternarius, Species.rufocinctus,
                Species.bimaculatus, Species.borealis, Species.vagans, Species.affinis,
                Species.griseocollis, Species.citrinus, Species.perplexus,
                Species.pensylvanicus, Species.sylvicola, Species.sandersoni,
                Species.nevadensis, Species.auricomus, Species.terricola,
                Species.fervidus, Species.flavifrons
        );

        List<Species> westernSpecies = Arrays.asList(
                Species.occidentalis, Species.melanopygus, Species.bifarius,
                Species.impatiens, Species.huntii, Species.ternarius, Species.terricola,
                Species.nevadensis, Species.vosnesenski, Species.cryptarum, Species.flavifrons,
                Species.griseocollis, Species.perplexus, Species.borealis, Species.rufocinctus,
                Species.mixtus, Species.centralis
        );

        mEasternAdapter = new PartsPickerAdapter(easternSpecies, Submission.BeeSpeciesType.Eastern);
        mWesternAdapter = new PartsPickerAdapter(westernSpecies, Submission.BeeSpeciesType.Western);
    }

    private void errorAndExit(String message) {
        getActivity().finish();
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDialogFinishClick(int id) {
        // User touched the dialog's finish button. Do nothing.
    }
}
