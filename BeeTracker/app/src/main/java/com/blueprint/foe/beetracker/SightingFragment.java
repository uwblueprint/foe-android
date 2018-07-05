package com.blueprint.foe.beetracker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by luisa on 2018-07-04.
 */
public class SightingFragment extends Fragment {
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.sighting_fragment, container, false);

        return view;
    }
}
