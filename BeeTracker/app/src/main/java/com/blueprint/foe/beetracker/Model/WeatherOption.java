package com.blueprint.foe.beetracker.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.blueprint.foe.beetracker.R;

/**
 * This class contains information about the weather component including whether or not it is selected,
 * its index and the drawable image to be loaded into the UI component.
 */
public class WeatherOption {
    private Submission.Weather mWeather;
    private int drawable;
    private boolean mSelected;

    public WeatherOption(Submission.Weather weather, int drawable) {
        this.mWeather = weather;
        this.mSelected = false;
        this.drawable = drawable;
    }

    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, drawable);
    }

    public Submission.Weather getWeather() {
        return mWeather;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelection(boolean selection) {
        this.mSelected = selection;
    }
}
