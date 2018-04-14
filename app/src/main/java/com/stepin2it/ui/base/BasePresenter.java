package com.stepin2it.ui.base;

public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mvpView;

    @Override
    public void attachView(T mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        mvpView = null;
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public T getMvpView() {
        return mvpView;
    }
}
