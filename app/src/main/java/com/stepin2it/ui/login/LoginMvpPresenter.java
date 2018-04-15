package com.stepin2it.ui.login;

import android.content.Context;

import com.stepin2it.data.DataManager;
import com.stepin2it.data.PreferenceHelper;
import com.stepin2it.data.models.RqLogin;
import com.stepin2it.data.models.RsToken;
import com.stepin2it.ui.base.BasePresenter;
import com.stepin2it.utils.IConstants;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginMvpPresenter extends BasePresenter<LoginMvpView>
        implements LoginValidation.LoginError {

    private LoginValidation loginValidation;
    private Context mContext;
    private DataManager mDataManager;

    LoginMvpPresenter(Context context, LoginValidation loginValidation) {
        this.loginValidation = loginValidation;
        mContext = context;
        mDataManager = DataManager.getInstance(context);
    }

    void login(final String userName, String password) {
        loginValidation.credentialCheck(userName, password, this);
        mDataManager.getToken(new RqLogin(userName, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RsToken>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RsToken rsToken) {
                        getMvpView().loginToken(rsToken.getToken());
                        PreferenceHelper.getInstance(mContext).writeString(IConstants.IPreference.PREF_TOKEN, rsToken.getToken());
                        PreferenceHelper.getInstance(mContext).writeString(IConstants.IPreference.PREF_USER_NAME, userName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().loginTokenError("Error getting token!");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void attachView(LoginMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    @Override
    public void onUserNameError() {
        getMvpView().setUserNameError();
    }

    @Override
    public void onPasswordError() {
        getMvpView().setPasswordError();
    }

    @Override
    public void validCredentials() {
        getMvpView().loginSuccessful();
    }
}
