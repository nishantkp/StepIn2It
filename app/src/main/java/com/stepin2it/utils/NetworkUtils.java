package com.stepin2it.utils;

import android.util.Log;

import com.stepin2it.ProductInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Network Utility class
 * Created by Nishant on 3/25/2018.
 */

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    /**
     * This method is used to get the token from https://reqres.in/
     * by providing username and password
     *
     * @return string token requested from https://reqres.in/
     */
    public static String getTokenFromReqres() {
        URL queryUrl = generateUrl(IConstants.IReqres.REQUEST_URL_STRING);
        String responseToken = null;
        try {
            responseToken = getTokenFromHttpRequest(queryUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception occurred while closing InputStream.", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error creating JSON object", e);
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
            Log.e(LOG_TAG, "Error creating url object", e);
        }
        return url;
    }

    /**
     * This method is used to get token string from URL
     *
     * @param requestUrl request url
     * @return string token
     * @throws IOException   Exception when creating http connection
     * @throws JSONException Exception when creating a JSON object
     */
    private static String getTokenFromHttpRequest(URL requestUrl) throws IOException, JSONException {
        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        String token = null;
        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put(IConstants.IReqres.KEY_JSON_EMAIL, IConstants.IReqres.USER_EMAIL);
        requestJsonObject.put(IConstants.IReqres.KEY_JSON_PASSWORD, IConstants.IReqres.USER_PASSWORD);
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
            if (httpsURLConnection.getResponseCode() == IConstants.SUCCESS_RESPONSE_CODE) {
                inputStream = httpsURLConnection.getInputStream();
                token = extractTokenFromJsonResponse(readDataFromInputStream(inputStream));
                Log.i(LOG_TAG, "token :" + token);
            } else {
                Log.i(LOG_TAG, "Response code : " + httpsURLConnection.getResponseCode());
                token = null;
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error opening Url connection", e);
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
        String line = null;
        try {
            line = reader.readLine();
            while (line != null) {
                outputString.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception occurred while reading line from BufferedReader", e);
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
            Log.e(LOG_TAG, "Error creating a JSON object", e);
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
            Log.i(LOG_TAG, "Exception caused by closing an InputStream", e);
        }
        if (jsonResponse != null) {
            return extractProductInfoFromJsonResponse(jsonResponse);
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
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readDataFromInputStream(inputStream);
            } else {
                // If received any other response(i.e 400) code return null JSON response
                Log.i(LOG_TAG, "Error response code : " + httpURLConnection.getResponseCode());
                jsonResponse = null;
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating a url connection", e);
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
     * This method is used to extract the features of JSON response
     *
     * @param jsonResponse JSON response in string format
     * @return List<ProductInfo> object which contains detail information about product
     */
    private static List<ProductInfo> extractProductInfoFromJsonResponse(String jsonResponse) {

        List<ProductInfo> productInfoList = new ArrayList<>();

        try {
            JSONObject productObject = new JSONObject(jsonResponse);
            if (productObject.has(IConstants.IJsonServer.KEY_JSON_PRODUCTS)) {
                JSONArray productsArray = productObject.getJSONArray(IConstants.IJsonServer.KEY_JSON_PRODUCTS);
                for (int i = 0; i < productsArray.length(); i++) {
                    JSONObject productDetail = productsArray.getJSONObject(i);

                    // Get the name of product if "name" field is present
                    String productName = null;
                    if (productDetail.has(IConstants.IJsonServer.KEY_JSON_NAME)) {
                        productName = productDetail.getString(IConstants.IJsonServer.KEY_JSON_NAME);
                    }

                    // Get the description of product if "description" field is present
                    String productDescription = null;
                    if (productDetail.has(IConstants.IJsonServer.KEY_JSON_DESCRIPTION)) {
                        productDescription = productDetail.getString(IConstants.IJsonServer.KEY_JSON_DESCRIPTION);
                    }

                    // Get the image-url of product if "image" field is present
                    String productImageUrl = null;
                    if (productDetail.has(IConstants.IJsonServer.KEY_JSON_IMAGE)) {
                        productImageUrl = productDetail.getString(IConstants.IJsonServer.KEY_JSON_IMAGE);
                    }

                    // Get the phone of product if "phone" field is present
                    String productPhone = null;
                    if (productDetail.has(IConstants.IJsonServer.KEY_JSON_PHONE)) {
                        productPhone = productDetail.getString(IConstants.IJsonServer.KEY_JSON_PHONE);
                    }

                    // Get the web-url of product if "web" field is present
                    String productWebUrl = null;
                    if (productDetail.has(IConstants.IJsonServer.KEY_JSON_WEB)) {
                        productWebUrl = productDetail.getString(IConstants.IJsonServer.KEY_JSON_WEB);
                    }

                    productInfoList.add(new ProductInfo(productName, productDescription
                            , productImageUrl, productPhone, productWebUrl));
                }
            }
        } catch (JSONException e) {
            Log.i(LOG_TAG, "Error creating a JSON object", e);
        }
        Log.i(LOG_TAG, "JSON response : " + jsonResponse);
        return productInfoList;
    }
}
