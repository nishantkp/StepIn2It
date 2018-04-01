package com.stepin2it.ui.models;

import com.google.gson.annotations.SerializedName;

/**
 * POJO for product information
 * Created by Nishant on 3/26/2018.
 */

public class ProductInfo {

    @SerializedName("name")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String productImageUrl;

    @SerializedName("phone")
    private String productPhone;

    @SerializedName("web")
    private String productWebUrl;

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductPhone() {
        return productPhone;
    }

    public String getProductWebUrl() {
        return productWebUrl;
    }
}
