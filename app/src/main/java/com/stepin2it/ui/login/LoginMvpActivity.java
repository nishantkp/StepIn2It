package com.stepin2it.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
        loginMvpPresenter = new LoginMvpPresenter(this);

        loginMvpPresenter.attachView(this);
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
