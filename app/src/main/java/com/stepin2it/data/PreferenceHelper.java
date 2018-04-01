package com.stepin2it.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.stepin2it.utils.IConstants;

/**
 * Created by Nishant on 3/25/2018.
 */

public class PreferenceHelper {
    private static PreferenceHelper sPreferenceHelper;
    private static SharedPreferences sSharedPreference;
    private static SharedPreferences.Editor sPreferenceEditor;

    public static PreferenceHelper getInstance(Context context) {
        if (sPreferenceHelper == null) {
            sPreferenceHelper = new PreferenceHelper();
            sSharedPreference = context.getSharedPreferences(IConstants.IPreference.PREF_NAME, Context.MODE_PRIVATE);
            sPreferenceEditor = sSharedPreference.edit();
        }
        return sPreferenceHelper;
    }

    public void writeString(String key, String value) {
        sPreferenceEditor.putString(key, value).apply();
    }

    public String readString(String key) {
        return sSharedPreference.getString(key, null);
    }
}
