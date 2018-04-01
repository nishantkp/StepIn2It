package com.stepin2it.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import timber.log.Timber;

/**
 * Network Utility class
 * Created by Nishant on 3/25/2018.
 */

public class NetworkUtils {

    /**
     * This method is used to get the token from https://reqres.in/
     * by providing username and password
     *
     * @param userName user-name/email entered by user
     * @param password password entered by user
     * @return string token requested from https://reqres.in/
     */
    public static String getTokenFromReqres(String userName, String password) {
        URL queryUrl = generateUrl(IConstants.IReqres.REQUEST_URL_STRING);
        String responseToken = null;
        try {
            responseToken = getTokenFromHttpRequest(queryUrl, userName, password);
        } catch (IOException e) {
            Timber.e(e, "Exception occurred while closing InputStream.");
        } catch (JSONException e) {
            Timber.e(e, "Error creating JSON object");
        }
        return responseToken;
    }

    /**
     * Generate URL object from url String
     *
     * @param urlString url in string format
     * @return URL object
     */
    private static URL generateUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Timber.e(e, "Error creating url object");
        }
        return url;
    }

    /**
     * This method is used to get token string from URL
     *
     * @param requestUrl request url
     * @param userName   user name entered by user
     * @param password   password entered by user
     * @return string token
     * @throws IOException   Exception when creating http connection
     * @throws JSONException Exception when creating a JSON object
     */
    private static String getTokenFromHttpRequest(URL requestUrl, String userName, String password)
            throws IOException, JSONException {
        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        String token = null;
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put(IConstants.IReqres.KEY_JSON_EMAIL, userName);
        requestJsonObject.put(IConstants.IReqres.KEY_JSON_PASSWORD, password);
        try {
            httpsURLConnection = (HttpsURLConnection) requestUrl.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            // Set the `Content-Type` for the data you are sending which is `application/json`
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.setDoInput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
            outputStreamWriter.write(requestJsonObject.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
            if (httpsURLConnection.getResponseCode() == IConstants.IReqres.SUCCESS_RESPONSE_CODE) {
                inputStream = httpsURLConnection.getInputStream();
                token = extractTokenFromJsonResponse(readDataFromInputStream(inputStream));
                Timber.i("token : %s", token);
            } else {
                Timber.i("Response code : %s", httpsURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Timber.e("Error opening Url connection");
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return token;
    }

    /**
     * This method is used to get data from inputStream
     *
     * @param inputStream InputStream object
     * @return json response string
     */
    private static String readDataFromInputStream(InputStream inputStream) {
        StringBuilder outputString = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                outputString.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Timber.e(e, "Exception occurred while reading line from BufferedReader");
        }
        return outputString.toString();
    }

    /**
     * Extract the value of token from json response
     *
     * @param jsonResponseString json string received from request
     * @return token
     */
    private static String extractTokenFromJsonResponse(String jsonResponseString) {
        String token = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonResponseString);
            if (jsonObject.has(IConstants.IReqres.KEY_JSON_TOKEN)) {
                token = jsonObject.getString(IConstants.IReqres.KEY_JSON_TOKEN);
            }
        } catch (JSONException e) {
            Timber.e(e, "Error creating a JSON object");
        }
        return token;
    }


    /**
     * This method is called to get the product information from url string
     * in List<ProductInfo> format
     *
     * @param urlString url string from which we want to get our product information
     * @return product information in List<ProductInfo> format
     */
    public static List<ProductInfo> fetchProductInfoFromUrl(String urlString) {
        if (urlString == null) {
            return null;
        }
        URL url = generateUrl(urlString);
        String jsonResponse = null;
        try {
            jsonResponse = getJsonFromHttpRequest(url);
        } catch (IOException e) {
            Timber.i("Exception caused by closing an InputStream");
        }
        if (jsonResponse != null) {
            Type productList = new TypeToken<List<ProductInfo>>() {
            }.getType();
            return new Gson().fromJson(jsonResponse, productList);
        } else {
            return null;
        }
    }

    /**
     * This method is called to get the JSON response in string format from URL object
     * by creating HTTP connection
     *
     * @param url URL object
     * @return JSON response string
     * @throws IOException Exception in method signature caused by closing an InputStream
     */
    private static String getJsonFromHttpRequest(URL url) throws IOException {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String jsonResponse = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(IConstants.IJsonServer.READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(IConstants.IJsonServer.CONNECT_TIME_OUT);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == IConstants.IJsonServer.SUCCESS_RESPONSE_CODE) {
                Timber.i("Response Code : %s", httpURLConnection.getResponseCode());
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readDataFromInputStream(inputStream);
            } else {
                // If received any other response(i.e 400) code return null JSON response
                Timber.i("Error response code : %s", httpURLConnection.getResponseCode());
                jsonResponse = null;
            }

        } catch (IOException e) {
            Timber.e(e, "Error creating a url connection");
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing an inputStream can throw IOException, which why getJsonFromHttpRequest
                // method signature specifies, throws IOException
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * This method is called to check whether the network is available or not
     *
     * @param context context of the app
     * @return If network is available method returns TRUE otherwise false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null
                && activeNetworkInfo.isConnected();
    }
}
