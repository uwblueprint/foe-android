package com.blueprint.foe.beetracker;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        TextView homeButton = (TextView) findViewById(R.id.buttonHome);
        homeButton.setTextColor(ContextCompat.getColor(this, R.color.grassGreen));
        homeButton.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.mipmap.icon_home_green), null, null);

        //set status bar color
        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mediumSkyCyan));

        TextView submitButton = (TextView) findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomePageActivity.this,
                        SubmissionActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
        });
        TextView historyButton = (TextView) findViewById(R.id.buttonHistory);
        historyButton.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HomePageActivity.this,
                        HistoryActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myIntent);
            }
        });

        TextView tvFOE = (TextView) findViewById(R.id.visit_foe);
        tvFOE.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.visit_foe_url)));
                startActivity(browserIntent);
            }
        });

        TextView tvLearnBeeCause = (TextView) findViewById(R.id.learn_bee_cause);
        tvLearnBeeCause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.learn_about_bee_cause_url)));
                startActivity(browserIntent);
            }
        });

        TextView tvDonate = (TextView) findViewById(R.id.donate);
        tvDonate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.donate_url)));
                startActivity(browserIntent);
            }
        });
    }
}
