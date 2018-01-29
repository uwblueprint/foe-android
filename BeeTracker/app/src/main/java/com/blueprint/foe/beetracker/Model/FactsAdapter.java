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

import java.util.ArrayList;

/**
 * An ArrayAdapter for the fact collection. Used by LearnActivity.
 */
public class FactsAdapter extends ArrayAdapter<Fact> {
    private static String TAG = FactsAdapter.class.toString();

    private ArrayList<Fact> mFacts;
    public FactsAdapter(Context context, ArrayList<Fact> facts) {
        super(context, 0, facts);
        this.mFacts = facts;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Fact fact = getItem(position);
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
        factId.setText("Tip #" + Integer.toString(fact.getId()));

        ImageView factImage = (ImageView) convertView.findViewById(R.id.imgFactIllustration);

        switch (fact.getCategory()) {
            case General:
                factImage.setImageResource(R.drawable.fact_illustation_general);
                break;
            case Water:
                factImage.setImageResource(R.drawable.fact_illustration_water);
                break;
            default:
                throw new Error("Fact Category not defined");
        }

        //set up tap event for X Close icon
        ImageView closeButton = (ImageView) convertView.findViewById(R.id.btnClose);

        closeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println( fact.getId() + " clicked!");
                mFacts.remove(fact);
                notifyDataSetChanged();
            }

        });

        // Complete button is a LinearLayout because it includes an ImageView for the checkmark as
        // well as the TextView saying done
        LinearLayout completeButton = (LinearLayout) convertView.findViewById(R.id.btnCompleteFact);
        final TextView completeText = (TextView) convertView.findViewById(R.id.lblCompleteCTA);
        final ImageView completeImage = (ImageView) convertView.findViewById(R.id.iconCompleteCTA);

        if (fact.isCompleted()) {
            completeText.setText(getContext().getString(R.string.completed));
            completeText.setTextColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
            completeImage.setImageResource(R.drawable.icon_done_checkmark_disabled);
        } else {
            completeText.setText(getContext().getString(R.string.done));
            completeText.setTextColor(ContextCompat.getColor(getContext(), R.color.grassGreen));
            completeImage.setImageResource(R.drawable.icon_done_checkmark);
        }

        completeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fact.setCompleted();
                notifyDataSetChanged();
            }

        });

        return convertView;
    }
}
