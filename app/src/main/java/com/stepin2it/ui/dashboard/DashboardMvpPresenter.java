package com.stepin2it.ui.dashboard;

import android.content.Context;

import com.stepin2it.data.DataManager;
import com.stepin2it.ui.base.BasePresenter;
import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DashboardMvpPresenter extends BasePresenter<DashboardMvpView> {
    DataManager mDataManager;

    public DashboardMvpPresenter(Context context) {
        mDataManager = DataManager.getInstance(context);
    }

    @Override
    public void attachView(DashboardMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void getProductInfo() {
        mDataManager.getProductInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ProductInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ProductInfo> productInfoList) {
                        getMvpView().loadProductData(productInfoList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().displayError("Error getting ProductList");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
