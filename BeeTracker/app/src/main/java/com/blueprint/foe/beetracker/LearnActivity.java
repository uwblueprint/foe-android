package com.blueprint.foe.beetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Model.FactCollection;
import com.blueprint.foe.beetracker.Model.FactsAdapter;
import com.blueprint.foe.beetracker.Model.StorageAccessor;

import java.io.IOException;

/*
 * Displays facts and call to actions for the user to read and act upon.
 *
 * Facts are initially loaded from a raw file and are stored to an internal file. On subsequent
 * LearnActivity launches, the facts are loaded from the internal file.
 */
public class LearnActivity extends AppCompatActivity {
    private static String TAG = LearnActivity.class.toString();

    private FactCollection mFacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        try {
            this.mFacts = StorageAccessor.loadFacts(this);
        } catch (IOException e) {
            Toast.makeText(this, "Could not load facts.", Toast.LENGTH_LONG);
            this.mFacts = new FactCollection();
        }

        final FactsAdapter adapter = new FactsAdapter(this, mFacts.getFacts());
        ListView listView = (ListView) findViewById(R.id.factsListView);

        TextView submitButton = (TextView) findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(LearnActivity.this,
                        SubmissionActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish(); // We want submissions to go back to the home page
                overridePendingTransition(0, 0); // no animation on activity finish
                startActivity(myIntent);
            }
        });
        TextView homeButton = (TextView) findViewById(R.id.buttonHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish(); // Home page is already on the stack, so just go back
                overridePendingTransition(0, 0);
            }
        });

        listView.setAdapter(adapter);
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            StorageAccessor.storeFacts(getBaseContext(), this.mFacts);
        } catch (IOException e){
            Log.e(TAG, "Could not store facts: " + e.toString());
            // Fail silently, they can see all their facts again
            // In the future, we may want to delete the facts file
        }
    }
}
