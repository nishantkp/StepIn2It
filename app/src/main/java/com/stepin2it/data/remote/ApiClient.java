package com.stepin2it.data.remote;

import com.stepin2it.utils.IConstants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        // Get the OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder().build();

        // Create a retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(IConstants.IJsonServer.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
