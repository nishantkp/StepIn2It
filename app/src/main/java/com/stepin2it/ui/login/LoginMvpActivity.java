package com.stepin2it.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.stepin2it.R;
import com.stepin2it.data.PreferenceHelper;
import com.stepin2it.ui.dashboard.DashBoardMvpActivity;
import com.stepin2it.utils.IConstants;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class LoginMvpActivity extends AppCompatActivity implements LoginMvpView {
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_fb_login)
    LoginButton btnFbLogin;

    LoginMvpPresenter loginMvpPresenter;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(LoginMvpActivity.this);
        facebookLoginSetUp();
        loginMvpPresenter = new LoginMvpPresenter(this);

        loginMvpPresenter.attachView(this);
    }

    private void facebookLoginSetUp() {
        callbackManager = CallbackManager.Factory.create();
        btnFbLogin.setReadPermissions(Arrays.asList("email"));
        btnFbLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d("Login Token : %s", loginResult.getAccessToken().getToken());
                PreferenceHelper.getInstance(LoginMvpActivity.this)
                        .writeString(IConstants.IPreference.PREF_TOKEN, loginResult.getAccessToken().getToken());
                loginToken(loginResult.getAccessToken().getToken());
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateLogin() {
        String emailAddress = edtUserName.getText().toString().trim();
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(emailAddress) || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            Toast.makeText(this, "Enter valid user name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_login)
    void login() {
        if (validateLogin()) {
            loginMvpPresenter.login(edtUserName.getText().toString().trim(), edtPassword.getText().toString());
        }
    }

    @Override
    public void loginToken(String token) {
        if (token != null) {
            startActivity(new Intent(this, DashBoardMvpActivity.class));
        }
    }

    @Override
    public void loginTokenError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
