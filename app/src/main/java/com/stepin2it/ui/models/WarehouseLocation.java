package com.stepin2it.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WarehouseLocation implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
    }

    public WarehouseLocation() {
    }

    protected WarehouseLocation(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
    }

    public static final Parcelable.Creator<WarehouseLocation> CREATOR = new Parcelable.Creator<WarehouseLocation>() {
        @Override
        public WarehouseLocation createFromParcel(Parcel source) {
            return new WarehouseLocation(source);
        }

        @Override
        public WarehouseLocation[] newArray(int size) {
            return new WarehouseLocation[size];
        }
    };
}
