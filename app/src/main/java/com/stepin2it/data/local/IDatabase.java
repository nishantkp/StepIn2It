package com.stepin2it.data.local;

public interface IDatabase {

    interface IProductTable {
        String ID = "id";
        String PRODUCT_NAME = "product_name";
        String PRODUCT_DESCRIPTION = "product_description";
        String PRODUCT_IMAGE = "image_url";
        String PRODUCT_PHONE = "phone";
        String TABLE_NAME = "product";
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY, "
                + PRODUCT_NAME + " TEXT, "
                + PRODUCT_DESCRIPTION + " TEXT, "
                + PRODUCT_IMAGE + " TEXT, "
                + PRODUCT_PHONE + " TEXT" + ")";
        String SQL_DELET_ENTRIES = "DELETE TABLE IF EXISTS " + TABLE_NAME;
    }

}
