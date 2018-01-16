package com.blueprint.foe.beetracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
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

    private FactCollection facts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        try {
            this.facts = StorageAccessor.loadFacts(this);
        } catch (IOException e) {
            Toast.makeText(this, "Could not load facts.", Toast.LENGTH_LONG);
            this.facts = new FactCollection();
        }

        final FactsAdapter adapter = new FactsAdapter(this, facts.getFacts());
        ListView listView = (ListView) findViewById(R.id.factsListView);

        listView.setAdapter(adapter);
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            StorageAccessor.storeFacts(getBaseContext(), this.facts);
        } catch (IOException e){
            Log.e(TAG, "Could not store facts: " + e.toString());
            // Fail silently, they can see all their facts again
            // In the future, we may want to delete the facts file
        }
    }
}
