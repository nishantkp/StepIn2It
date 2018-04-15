package com.stepin2it.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.stepin2it.R;
import com.stepin2it.ui.dashboard.DashBoardMvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginMvpActivity extends AppCompatActivity implements LoginMvpView {
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;

    LoginMvpPresenter loginMvpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(LoginMvpActivity.this);
        loginMvpPresenter = new LoginMvpPresenter(this, new CredentialCheck());

        loginMvpPresenter.attachView(this);
    }

    @OnClick(R.id.btn_login)
    void login() {
        loginMvpPresenter.login(edtUserName.getText().toString().trim(), edtPassword.getText().toString());
    }

    @Override
    public void loginSuccessful() {
        Toast.makeText(this, "Valid username and password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserNameError() {
        Toast.makeText(this, "Enter valid user email!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPasswordError() {
        Toast.makeText(this, "Enter a password!", Toast.LENGTH_SHORT).show();
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
