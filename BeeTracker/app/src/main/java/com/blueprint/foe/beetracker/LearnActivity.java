package com.blueprint.foe.beetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

        facts.addFact("Don\'t Mow So Low", "Mow so low.");
        facts.addFact("Stop Sprinkler Irrigation", "Stop sprinkler irrigation because it's bad.");

        final FactsAdapter adapter = new FactsAdapter(this, facts.getFacts());
        ListView listView = (ListView) findViewById(R.id.factsListView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View view, final int position, long id) {
                LinearLayout completeButton = (LinearLayout) view.findViewById(R.id.btnCompleteFact);

                completeButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        System.out.println( position + " clicked!");
                        facts.getFacts().remove(position);

                        adapter.notifyDataSetChanged();

                    }

                });
            }
        });

    };
}
