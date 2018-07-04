package com.blueprint.foe.beetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueprint.foe.beetracker.API.BeeTrackerCaller;
import com.blueprint.foe.beetracker.API.TokenHelper;
import com.blueprint.foe.beetracker.Listeners.BeeAlertDialogListener;
import com.blueprint.foe.beetracker.Model.SubmissionsAdapter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that shows the user all of their previous submissions.
 */
public class HistoryActivity extends AppCompatActivity implements BeeAlertDialogListener {
    private static final String TAG = HistoryActivity.class.toString();

    private Callback allSightingsCallback;
    private ListView listView;
    private TextView tvNumberOfSightings;
    private RelativeLayout rlEmptyHistory;
    private TextView tvSubmitInstruction;

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
                TokenHelper.setSharedPreferencesFromHeader(HistoryActivity.this, response.headers());
                if (response.code() == 422 || response.body() == null) {
                    Log.e(TAG, "The response from the server is " + response.code() + " " + response.message());
                    showErrorDialog(getString(R.string.error_message_history));
                    return;
                }

                BeeTrackerCaller.SubmissionResponse[] submissions = response.body();
                if (submissions != null) {
                    if (submissions.length == 0) {
                        return;
                    }

                    rlEmptyHistory.setVisibility(View.INVISIBLE);
                    SubmissionsAdapter adapter = new SubmissionsAdapter(HistoryActivity.this, submissions);
                    listView.setAdapter(adapter);
                    if (submissions.length == 1) {
                        tvNumberOfSightings.setText(getString(R.string.history_one_sighting));
                    } else {
                        tvNumberOfSightings.setText(getString(R.string.history_plural_sightings, submissions.length));
                    }
                }
            }

            @Override
            public void onFailure(Call<BeeTrackerCaller.SubmissionResponse[]> call, Throwable t) {
                Log.e(TAG, "There was an error with the submitCallback + " + t.toString());
                t.printStackTrace();
                showErrorDialog(getString(R.string.error_message_history));
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
            showErrorDialog(getString(R.string.error_message_history));
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
        rlEmptyHistory = (RelativeLayout) findViewById(R.id.emptyHistory);
        tvSubmitInstruction = (TextView) findViewById(R.id.submit_instruction);

        SpannableStringBuilder str = new SpannableStringBuilder(getString(R.string.history_submit_instruction));
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 25, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSubmitInstruction.setText(str);
    }

    private void showErrorDialog(String message) {
        BeeAlertErrorDialog dialog = new BeeAlertErrorDialog();
        Bundle args = new Bundle();
        args.putString(BeeAlertErrorDialog.ERROR_MESSAGE_KEY, message);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "ErrorMessage");
    }


    @Override
    public void onDialogFinishClick(int id) {}
}
