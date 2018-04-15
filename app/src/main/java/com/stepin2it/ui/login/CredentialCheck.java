package com.stepin2it.ui.login;

import android.text.TextUtils;

public class CredentialCheck implements LoginValidation {
    @Override
    public void credentialCheck(String userName, String password, LoginError loginError) {
        if (TextUtils.isEmpty(userName) || !android.util.Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            loginError.onUserNameError();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginError.onPasswordError();
            return;
        }
        loginError.validCredentials();
    }
}
