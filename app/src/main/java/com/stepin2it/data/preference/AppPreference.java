package com.stepin2it.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.stepin2it.utils.IConstants;

public class AppPreference{

    private static com.stepin2it.data.PreferenceHelper sPreferenceHelper;
    private static SharedPreferences sSharedPreference;
    private static SharedPreferences.Editor sPreferenceEditor;

    public static com.stepin2it.data.PreferenceHelper getInstance(Context context) {
        if (sPreferenceHelper == null) {
            sPreferenceHelper = new com.stepin2it.data.PreferenceHelper();
            sSharedPreference = context.getSharedPreferences(IConstants.IPreference.PREF_NAME, Context.MODE_PRIVATE);
            sPreferenceEditor = sSharedPreference.edit();
        }
        return sPreferenceHelper;
    }
}
