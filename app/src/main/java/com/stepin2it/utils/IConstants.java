package com.stepin2it.utils;

/**
 * Created by Nishant on 3/25/2018.
 */

public interface IConstants {
    String KEY_PRODUCT_IMAGE_INTENT = "KEY_PRODUCT_IMAGE_INTENT";
    String PRODUCT_IMAGE_FILE_NAME = "PRODUCT_IMAGE_FILE_NAME";

    // Login detail information for https://reqres.in
    interface IReqres {
        int SUCCESS_RESPONSE_CODE = 201;
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

    // Fake Online REST server : https://my-json-server.typicode.com/
    // Get json data from devStepin2IT github account
    interface IJsonServer {
        String REQUEST_URL = "http://my-json-server.typicode.com/devStepin2IT/apitest/productData";
        int READ_TIME_OUT = 10000;
        int CONNECT_TIME_OUT = 15000;
        int SUCCESS_RESPONSE_CODE = 200;
    }
}

