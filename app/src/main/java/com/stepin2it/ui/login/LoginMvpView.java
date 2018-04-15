package com.stepin2it.ui.login;

import com.stepin2it.ui.base.MvpView;

public interface LoginMvpView extends MvpView {

    void loginToken(String token);

    void loginTokenError(String error);
}
