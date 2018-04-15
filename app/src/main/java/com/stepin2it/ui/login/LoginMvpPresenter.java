package com.stepin2it.ui.login;

import android.content.Context;

import com.stepin2it.data.DataManager;
import com.stepin2it.data.models.RqLogin;
import com.stepin2it.data.models.RsToken;
import com.stepin2it.ui.base.BasePresenter;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginMvpPresenter extends BasePresenter<LoginMvpView> {

    private DataManager mDataManager;

    LoginMvpPresenter(Context context) {
        mDataManager = DataManager.getInstance(context);
    }

    void login(String userName, String password) {
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
}
