package com.stepin2it.utils;

/**
 * Created by Nishant on 3/25/2018.
 */

public interface IConstants {
    String KEY_PRODUCT_IMAGE_INTENT = "KEY_PRODUCT_IMAGE_INTENT";
    String PRODUCT_IMAGE_FILE_NAME = "PRODUCT_IMAGE_FILE_NAME";
    String KEY_PRODUCT_IMAGE_URL = "KEY_PRODUCT_IMAGE_URL";
    String KEY_PRODUCT_DETAIL_PARCELABLE = "KEY_PRODUCT_DETAIL_PARCELABLE";

    // Login detail information for https://reqres.in
    interface IReqres {
        int SUCCESS_RESPONSE_CODE = 201;
        String REQUEST_URL_STRING = "https://reqres.in/api/register";
        String BASE_URL = "https://reqres.in/";
        String KEY_JSON_EMAIL = "email";
        String KEY_JSON_PASSWORD = "password";
        String KEY_JSON_TOKEN = "token";
    }

    interface IPreference {
        String PREF_USER_NAME = "PREF_USER_NAME";
        String PREF_NAME = "stepin2it";
        String PREF_TOKEN = "PREF_TOKEN";
    }

    // Fake Online REST server : https://my-json-server.typicode.com/
    // Get json data from devStepin2IT github account
    interface IJsonServer {
        String REQUEST_URL = "http://my-json-server.typicode.com/nishantkp/apitest/productData";
        String BASE_URL = "https://my-json-server.typicode.com/";
        int READ_TIME_OUT = 10000;
        int CONNECT_TIME_OUT = 15000;
        int SUCCESS_RESPONSE_CODE = 200;
    }

    interface IActions {
        String START_MUSIC_PLAYER = "START_MUSIC_PLAYER";
        String STOP_MUSIC_PLAYER = "STOP_MUSIC_PLAYER";
        String ACTION_MAIN = "ACTION_MAIN";
        String ACTION_PLAY = "ACTION_PLAY";
        String ACTION_PAUSE = "ACTION_PAUSE";
        String ACTION_NEXT = "ACTION_NEXT";
        String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    }

    interface INotification {
        int ID_MUSIC_NOTIFICATION = 100;
    }
}

