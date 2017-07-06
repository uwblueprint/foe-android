package com.blueprint.foe.beetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // Locate the button in activity_learn.xml
        Button submitButton = (Button) findViewById(R.id.button2);

        // Capture button clicks
        submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start Submission Activity
                Intent myIntent = new Intent(HomePageActivity.this,
                        SubmissionActivity.class);
                startActivity(myIntent);
            }
        });
        // Locate the button in activity_home_page.xml
        Button learnButton = (Button) findViewById(R.id.button3);

        // Capture button clicks
        learnButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {

                // Start Learn Activity
                Intent myIntent = new Intent(HomePageActivity.this,
                        LearnActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
