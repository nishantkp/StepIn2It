package com.stepin2it;

/**
 * POJO for product information
 * Created by Nishant on 3/26/2018.
 */

public class ProductInfo {

    private String productName;
    private String productDescription;
    private String productImageUrl;
    private String productPhone;
    private String productWebUrl;

    ProductInfo(String productName, String productDescription, String productImageUrl
            , String productPhone, String productWebUrl) {

        this.productName = productName;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productPhone = productPhone;
        this.productWebUrl = productWebUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
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
