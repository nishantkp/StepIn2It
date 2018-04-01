package com.stepin2it.ui.models;

import com.google.gson.annotations.SerializedName;

public class WarehouseLocation {
    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
