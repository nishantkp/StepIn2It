package com.stepin2it.ui.login;

public interface LoginValidation {
    interface LoginError {
        void onUserNameError();

        void onPasswordError();

        void validCredentials();
    }

    void credentialCheck(String userName, String password, LoginError loginError);
}
