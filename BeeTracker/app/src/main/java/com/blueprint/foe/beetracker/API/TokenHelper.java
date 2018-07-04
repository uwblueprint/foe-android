package com.blueprint.foe.beetracker.API;

import android.content.Context;
import android.content.SharedPreferences;

import com.blueprint.foe.beetracker.R;

import okhttp3.Headers;

/**
 * Helper method to set the proper fields in SharedPreferences when a response returns from the server
 */
public class TokenHelper {
    public static void setSharedPreferencesFromHeader(Context context, Headers headers) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (headers.get("access-token") != null && !headers.get("access-token").isEmpty()) {
            sharedPref.edit().putString(context.getString(R.string.preference_login_token), headers.get("access-token")).commit();
        }
        sharedPref.edit().putString(context.getString(R.string.preference_login_token_type), headers.get("token-type")).commit();
        sharedPref.edit().putString(context.getString(R.string.preference_login_client), headers.get("client")).commit();
        sharedPref.edit().putString(context.getString(R.string.preference_login_expiry), headers.get("expiry")).commit();
        sharedPref.edit().putString(context.getString(R.string.preference_login_uid), headers.get("uid")).commit();
    }
}
