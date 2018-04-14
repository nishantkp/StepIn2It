package com.stepin2it.ui.dashboard;

import com.stepin2it.ui.base.MvpView;
import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

public interface DashboardMvpView extends MvpView {
    void loadProductData(List<ProductInfo> productInfoList);

    void displayError(String message);
}
