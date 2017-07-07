package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by luisa on 2017-07-07.
 */

public class ReviewFragment  extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.review_fragment, container, false);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show popup then go back to home page
                getActivity().finish();
            }
        });
        Button backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                Fragment newFragment = new IdentifyFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });
        return view;
    }
}
