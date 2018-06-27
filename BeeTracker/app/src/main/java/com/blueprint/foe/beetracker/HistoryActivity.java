package com.blueprint.foe.beetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.Model.CompletedSubmission;
import com.blueprint.foe.beetracker.Model.CurrentSubmission;
import com.blueprint.foe.beetracker.Model.StorageAccessor;
import com.blueprint.foe.beetracker.Model.SubmissionCollection;
import com.blueprint.foe.beetracker.Model.SubmissionsAdapter;

import java.io.IOException;
import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that shows the user all of their previous submissions.
 */
public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.toString();

    private SubmissionCollection mSubmissions;
    private Callback allSightingsCallback;
    private ListView listView;
    private TextView tvNumberOfSightings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        allSightingsCallback = new Callback<BeeTrackerCaller.SubmissionResponse[]>() {
            @Override
            public void onResponse(Call<BeeTrackerCaller.SubmissionResponse[]> call, Response<BeeTrackerCaller.SubmissionResponse[]> response) {
                if (response.code() == 401) {
                    Log.e(TAG, "The response from the server is " + response.code() + " " + response.message());
                    Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(intent);
                    (HistoryActivity.this).finish();
                    Toast.makeText(HistoryActivity.this, "Sorry, you've been logged out.", Toast.LENGTH_LONG).show();
                    return;
                }
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                sharedPref.edit().putString(getString(R.string.preference_login_token), response.headers().get("access-token")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_token_type), response.headers().get("token-type")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_client), response.headers().get("client")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_expiry), response.headers().get("expiry")).commit();
                sharedPref.edit().putString(getString(R.string.preference_login_uid), response.headers().get("uid")).commit();
                if (response.code() == 422 || response.body() == null) {
                    Log.e(TAG, "The response from the server is " + response.code() + " " + response.message());
                    return;
                }
                Log.d(TAG, "The response body: " + response.body());

                if (response.body().length == 0) {
                    return;
                }
                SubmissionsAdapter adapter = new SubmissionsAdapter(HistoryActivity.this, response.body());
                listView.setAdapter(adapter);

                if (response.body().length == 1) {
                    tvNumberOfSightings.setText("1 sighting");
                } else {
                    tvNumberOfSightings.setText(response.body().length + " sightings");
                }

            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SubmissionResponse[]> call, Throwable t) {
                Log.e(TAG, "There was an error with the submitCallback + " + t.toString());
                t.printStackTrace();
            }
        };
        BeeTrackerCaller caller = new BeeTrackerCaller();
        try {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String accessToken = sharedPref.getString(getString(R.string.preference_login_token), null);
            String tokenType = sharedPref.getString(getString(R.string.preference_login_token_type), null);
            String client = sharedPref.getString(getString(R.string.preference_login_client), null);
            String uid = sharedPref.getString(getString(R.string.preference_login_uid), null);
            Call<BeeTrackerCaller.SubmissionResponse[]> token = caller.getAllSightings(accessToken, tokenType, client, uid);
            token.enqueue(allSightingsCallback);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            //showErrorDialog(getString(R.string.error_message_login));
        }

        listView = (ListView) findViewById(R.id.sightingsListView);

        TextView submitButton = (TextView) findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(HistoryActivity.this,
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
        TextView historyButton = (TextView) findViewById(R.id.buttonHistory);
        historyButton.setTextColor(ContextCompat.getColor(this, R.color.grassGreen));
        historyButton.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.lightbulb_green_icon), null, null, null);

        tvNumberOfSightings = (TextView) findViewById(R.id.numSightings);

    }
}
