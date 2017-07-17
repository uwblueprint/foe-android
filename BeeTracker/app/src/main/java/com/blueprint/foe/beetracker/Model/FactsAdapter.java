package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blueprint.foe.beetracker.R;

import org.w3c.dom.Text;

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
        final FactCollection.Fact fact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fact_view, parent, false);
        }

        //template fact data (illustration, text) into view
        TextView factTitle = (TextView) convertView.findViewById(R.id.factTitle);
        TextView factDescription = (TextView) convertView.findViewById(R.id.factDescription);
        TextView factId = (TextView) convertView.findViewById(R.id.lblFactId);

        factTitle.setText(fact.getTitle());
        factDescription.setText(fact.getDescription());
        factId.setText("Tip #" + Integer.toString(fact.getId()+1));

        ImageView factImage = (ImageView) convertView.findViewById(R.id.imgFactIllustration);

        switch (fact.getCategory()) {
            case GENERAL:
                factImage.setImageResource(R.drawable.fact_illustation_general);
                break;
            case WATER:
                factImage.setImageResource(R.drawable.fact_illustration_water);
                break;
            default:
                throw new Error("Fact Category not defined");
        }

        //set up tap event for X Close icon
        LinearLayout closeButton = (LinearLayout) convertView.findViewById(R.id.btnClose);

        closeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( fact.getId() + " clicked!");
                remove(fact);
            }

        });

        //set up event for completing a fact
        LinearLayout completeButton = (LinearLayout) convertView.findViewById(R.id.btnCompleteFact);
        final TextView completeText = (TextView) convertView.findViewById(R.id.lblCompleteCTA);
        final ImageView completeImage = (ImageView) convertView.findViewById(R.id.iconCompleteCTA);

        completeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeText.setText("Completed");
                completeText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_grey));
                completeImage.setImageResource(R.drawable.icon_done_checkmark_disabled);
            }

        });

        return convertView;
    }
}
