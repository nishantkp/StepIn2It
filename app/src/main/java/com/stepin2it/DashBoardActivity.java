package com.stepin2it;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stepin2it.utils.IConstants;
import com.stepin2it.utils.NetworkUtils;
import com.stepin2it.utils.PreferenceHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity {
    @BindView(R.id.txt_user_email_id)
    TextView txtUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(DashBoardActivity.this);

        txtUserEmailId = findViewById(R.id.txt_user_email_id);

        String userName
                = PreferenceHelper.getInstance(DashBoardActivity.this).readString(IConstants.IPreference.PREF_USER_NAME);
        txtUserEmailId.setText(userName);
        // Execute async task
        new ProductList().execute(IConstants.IJsonServer.REQUEST_URL);
    }

    // ProductList class to perform network operation in background thread
    private static class ProductList extends AsyncTask<String, Void, List<ProductInfo>> {

        @Override
        protected List<ProductInfo> doInBackground(String... strings) {
            return NetworkUtils.fetchProductInfoFromUrl(strings[0]);
        }

        @Override
        protected void onPostExecute(List<ProductInfo> productInfos) {
            super.onPostExecute(productInfos);
        }
    }
}
