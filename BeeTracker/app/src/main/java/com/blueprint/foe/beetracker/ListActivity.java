package com.blueprint.foe.beetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.Model.FactCollection;
import com.blueprint.foe.beetracker.Model.FactsAdapter;
import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.blueprint.foe.beetracker.Model.SubmissionCollection;
import com.blueprint.foe.beetracker.Model.SubmissionsAdapter;

import java.io.IOException;

/**
 * Created by luisa on 2018-04-12.
 */

public class ListActivity extends AppCompatActivity {

    private SubmissionCollection mSubmissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        try {
            this.mSubmissions = StorageAccessor.loadSubmissions(2);
        } catch (IOException e) {
            Toast.makeText(this, "Could not load facts.", Toast.LENGTH_LONG);
            this.mSubmissions = new SubmissionCollection();
        }

        final SubmissionsAdapter adapter = new SubmissionsAdapter(this, mSubmissions.getSubmissions());
        ListView listView = (ListView) findViewById(R.id.sightingsListView);

        TextView submitButton = (TextView) findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(ListActivity.this,
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
        TextView listButton = (TextView) findViewById(R.id.buttonList);
        listButton.setTextColor(ContextCompat.getColor(this, R.color.grassGreen));
        listButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.lightbulb_green_icon), null, null, null);

        listView.setAdapter(adapter);
    }
}
