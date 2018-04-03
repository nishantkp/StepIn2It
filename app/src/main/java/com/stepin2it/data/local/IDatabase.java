package com.stepin2it.data.local;

public interface IDatabase {

    interface IProductTable {
        String ID = "id";
        String PRODUCT_NAME = "product_name";
        String PRODUCT_DESCRIPTION = "product_description";
        String PRODUCT_IMAGE = "image_url";
        String PRODUCT_PHONE = "phone";
        String TABLE_NAME = "product";
        String PRODUCT_WEB = "web_url";
        String PRODUCT_PRICE = "price";
        String PRODUCT_LENGTH = "length";
        String PRODUCT_WIDTH = "width";
        String PRODUCT_HEIGHT = "height";
        String WAREHOUSE_LATITUDE = "warehouse_latitude";
        String WAREHOUSE_LONGITUDE = "warehouse_longitude";
        // Raw SQL statement for creating a new table
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY, "
                + PRODUCT_NAME + " TEXT, "
                + PRODUCT_DESCRIPTION + " TEXT, "
                + PRODUCT_IMAGE + " TEXT, "
                + PRODUCT_PHONE + " TEXT, "
                + PRODUCT_WEB + " TEXT, "
                + PRODUCT_PRICE + " TEXT, "
                + PRODUCT_LENGTH + " TEXT, "
                + PRODUCT_WIDTH + " TEXT, "
                + PRODUCT_HEIGHT + " TEXT, "
                + WAREHOUSE_LATITUDE + " TEXT, "
                + WAREHOUSE_LONGITUDE + " TEXT" + ")";
        // Raw SQL statement for delete table is it exists in database
        String SQL_DELET_ENTRIES = "DELETE TABLE IF EXISTS " + TABLE_NAME;
    }

}
