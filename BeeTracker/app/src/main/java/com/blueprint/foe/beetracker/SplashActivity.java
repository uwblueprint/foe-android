package com.blueprint.foe.beetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Splash screen shows a bee on a yellow background. This is specified in the SplashTheme.
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.preference_login_token), "");
        Log.d(TAG, token);
        Intent intent = null;
        if (token.isEmpty()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, HomePageActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
