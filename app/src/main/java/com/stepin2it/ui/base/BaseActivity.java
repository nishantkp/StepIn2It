package com.stepin2it.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stepin2it.data.local.DatabaseHelper;
import com.stepin2it.data.remote.ApiClient;
import com.stepin2it.data.remote.ApiInterface;

public class BaseActivity extends AppCompatActivity {
    public ApiInterface mApiInterface;
    public DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mDatabaseHelper = DatabaseHelper.getInstance(BaseActivity.this);
    }
}
