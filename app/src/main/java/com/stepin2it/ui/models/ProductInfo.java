package com.stepin2it.ui.models;

import com.google.gson.annotations.SerializedName;

/**
 * POJO for product information - according to JSON object
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

    @SerializedName("price")
    private String price;

    @SerializedName("tags")
    private String[] tags;

    @SerializedName("dimensions")
    private Dimensions dimensions;

    @SerializedName("warehouseLocation")
    private WarehouseLocation warehouseLocation;

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

    public String getPrice() {
        return price;
    }

    public String[] getTags() {
        return tags;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public WarehouseLocation getWarehouseLocation() {
        return warehouseLocation;
    }
}
