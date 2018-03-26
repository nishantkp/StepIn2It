package com.stepin2it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.stepin2it.utils.IConstants;
import com.stepin2it.utils.PreferenceHelper;

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
            launchDashBoard();
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
        } else if (!emailAddress.equals(IConstants.USER_NAME) || !password.equals(IConstants.PASSWORD)) {
            Toast.makeText(LoginActivity.this, "User name or password incorrect!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void launchDashBoard() {
        PreferenceHelper.getInstance(LoginActivity.this)
                .writeString(IConstants.IPreference.PREF_USER_NAME, edtUserName.getText().toString().trim());
        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(intent);
    }
}
