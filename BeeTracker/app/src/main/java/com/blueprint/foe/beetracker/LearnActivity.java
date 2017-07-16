package com.blueprint.foe.beetracker;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueprint.foe.beetracker.Model.FactCollection;
import com.blueprint.foe.beetracker.Model.FactsAdapter;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    public static FactCollection facts = new FactCollection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        facts.addFact("Don\'t Mow So Low",
                "Mow lawns with a high blade setting to allow violets and clovers to grow.");
        facts.addFact("Stop Sprinkler Irrigation",
                "Bees can perceive imminent rain and will seak protection in their nests. Sprinkler irrigation offers no warning cues and can alter visual landmarks and entrance to nests.",
                FactCollection.Category.WATER);
        facts.addFact("Stop Fertilizing",
                "Native plants do not need it, and fertilizing only encourages weeds and invasive species.");

        final FactsAdapter adapter = new FactsAdapter(this, facts.getFacts());
        ListView listView = (ListView) findViewById(R.id.factsListView);

        listView.setAdapter(adapter);
    };
}
