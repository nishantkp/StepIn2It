package com.stepin2it.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Dimensions implements Parcelable {
    @SerializedName("length")
    private String length;

    @SerializedName("width")
    private String width;

    @SerializedName("height")
    private String height;

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.length);
        dest.writeString(this.width);
        dest.writeString(this.height);
    }

    public Dimensions() {
    }

    protected Dimensions(Parcel in) {
        this.length = in.readString();
        this.width = in.readString();
        this.height = in.readString();
    }

    public static final Parcelable.Creator<Dimensions> CREATOR = new Parcelable.Creator<Dimensions>() {
        @Override
        public Dimensions createFromParcel(Parcel source) {
            return new Dimensions(source);
        }

        @Override
        public Dimensions[] newArray(int size) {
            return new Dimensions[size];
        }
    };
}
