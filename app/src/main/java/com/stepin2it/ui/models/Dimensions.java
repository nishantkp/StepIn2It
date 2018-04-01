package com.stepin2it.ui.models;

import com.google.gson.annotations.SerializedName;

public class Dimensions {
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
}
