package com.stepin2it.utils;

/**
 * Created by Nishant on 3/25/2018.
 */

public interface IConstants {
    String USER_NAME = "nishantuwindsor@gmail.com";
    String PASSWORD = "12345";
    int SUCCESS_RESPONSE_CODE = 201;

    // Login detail information for https://reqres.in
    interface IReqres {
        String USER_EMAIL = "sydney@fife";
        String USER_PASSWORD = "pistol";
        String REQUEST_URL_STRING = "https://reqres.in/api/register";
        String KEY_JSON_EMAIL = "email";
        String KEY_JSON_PASSWORD = "password";
        String KEY_JSON_TOKEN = "token";
    }

    interface IPreference {
        String PREF_USER_NAME = "PREF_USER_NAME";
        String PREF_NAME = "stepin2it";
        String PREF_TOKEN = "PREF_TOKEN";
    }
}

