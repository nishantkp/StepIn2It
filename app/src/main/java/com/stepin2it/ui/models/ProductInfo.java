package com.stepin2it.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * POJO for product information - according to JSON object
 * Created by Nishant on 3/26/2018.
 */

public class ProductInfo implements Parcelable {

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

    public ProductInfo(String productName, String description, String productImageUrl,
                       String productPhone, String productWebUrl, String price, String[] tags,
                       Dimensions dimensions, WarehouseLocation warehouseLocation) {
        this.productName = productName;
        this.description = description;
        this.productImageUrl = productImageUrl;
        this.productPhone = productPhone;
        this.productWebUrl = productWebUrl;
        this.price = price;
        this.tags = tags;
        this.dimensions = dimensions;
        this.warehouseLocation = warehouseLocation;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productName);
        dest.writeString(this.description);
        dest.writeString(this.productImageUrl);
        dest.writeString(this.productPhone);
        dest.writeString(this.productWebUrl);
        dest.writeString(this.price);
        dest.writeStringArray(this.tags);
        dest.writeParcelable(this.dimensions, flags);
        dest.writeParcelable(this.warehouseLocation, flags);
    }

    public ProductInfo() {
    }

    protected ProductInfo(Parcel in) {
        this.productName = in.readString();
        this.description = in.readString();
        this.productImageUrl = in.readString();
        this.productPhone = in.readString();
        this.productWebUrl = in.readString();
        this.price = in.readString();
        this.tags = in.createStringArray();
        this.dimensions = in.readParcelable(Dimensions.class.getClassLoader());
        this.warehouseLocation = in.readParcelable(WarehouseLocation.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductInfo> CREATOR = new Parcelable.Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
}
