package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueprint.foe.beetracker.R;

import java.util.ArrayList;

/**
 * Created by johnsington on 2017-07-08.
 */

public class FactsAdapter extends ArrayAdapter<FactCollection.Fact> {
    public FactsAdapter(Context context, ArrayList<FactCollection.Fact> facts) {
        super(context, 0, facts);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FactCollection.Fact fact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fact_view, parent, false);
        }

        TextView factTitle = (TextView) convertView.findViewById(R.id.factTitle);
        TextView factDescription = (TextView) convertView.findViewById(R.id.factDescription);

        factTitle.setText(fact.getTitle());
        factDescription.setText(fact.getDescription());

        return convertView;
    }
}
