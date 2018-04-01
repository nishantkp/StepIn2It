package com.stepin2it.ui.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.stepin2it.R;
import com.stepin2it.utils.IConstants;
import com.stepin2it.data.NetworkUtils;
import com.stepin2it.data.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(LoginActivity.this);
    }

    @OnClick(R.id.btn_login)
    void onLoginButtonClick() {
        if (validateLogin()) {
            if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                TokenAsyncTask networkAsyncTask = new TokenAsyncTask(LoginActivity.this);
                networkAsyncTask
                        .execute(edtUserName.getText().toString().trim(),
                                edtPassword.getText().toString());
            } else {
                Toast.makeText(LoginActivity.this, "Enable your internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateLogin() {
        String emailAddress = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(emailAddress) || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            Toast.makeText(LoginActivity.this, "Enter valid user name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void launchDashBoard() {
        PreferenceHelper.getInstance(LoginActivity.this)
                .writeString(IConstants.IPreference.PREF_USER_NAME, edtUserName.getText().toString().trim());
        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * AsyncTask for making network calls to generate token
     */
    @SuppressLint("StaticFieldLeak")
    private class TokenAsyncTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;
        private Context context;

        TokenAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(
                    this.context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String userName = strings[0];
            String password = strings[1];
            //            for (int i = 0; i < 10; i++) {
//                try {
//                    Thread.sleep(1000);
//                    publishProgress(i);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            return NetworkUtils.getTokenFromReqres(userName, password);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("Progress = " + String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(String tokenString) {
            progressDialog.cancel();
            if (tokenString == null) {
                Toast.makeText(LoginActivity.this, "Credentials are not valid!", Toast.LENGTH_SHORT).show();
            } else {
                PreferenceHelper.getInstance(LoginActivity.this)
                        .writeString(IConstants.IPreference.PREF_TOKEN, tokenString);
                launchDashBoard();
            }
        }
    }
}
