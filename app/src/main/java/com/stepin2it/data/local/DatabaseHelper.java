package com.stepin2it.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

public class DatabaseHelper {
    private static DatabaseHelper sDatabaseHelper;
    private static SQLiteDatabase sDatabase;

    public static DatabaseHelper getInstance(Context context) {
        if (sDatabaseHelper == null) {
            sDatabaseHelper = new DatabaseHelper();
            sDatabase = new DbOpenHelper(context).getWritableDatabase();
        }
        return sDatabaseHelper;
    }

    /**
     * Call this method to insert List<ProductInfo> into database.
     * Insert method requires data in form of ContentValue object, so create a ContentValue
     * object and use for-each to loop to insert data into database.
     *
     * @param productInfoList List of product
     */
    public void insertProducts(List<ProductInfo> productInfoList) {
        for (ProductInfo productInfo : productInfoList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(IDatabase.IProductTable.PRODUCT_NAME, productInfo.getProductName());
            contentValues.put(IDatabase.IProductTable.PRODUCT_DESCRIPTION, productInfo.getDescription());
            contentValues.put(IDatabase.IProductTable.PRODUCT_IMAGE, productInfo.getProductImageUrl());
            contentValues.put(IDatabase.IProductTable.PRODUCT_PHONE, productInfo.getProductPhone());
            contentValues.put(IDatabase.IProductTable.PRODUCT_WEB, productInfo.getProductWebUrl());
            contentValues.put(IDatabase.IProductTable.PRODUCT_PRICE, productInfo.getPrice());
            contentValues.put(IDatabase.IProductTable.PRODUCT_LENGTH, productInfo.getDimensions().getLength());
            contentValues.put(IDatabase.IProductTable.PRODUCT_WIDTH, productInfo.getDimensions().getWidth());
            contentValues.put(IDatabase.IProductTable.PRODUCT_HEIGHT, productInfo.getDimensions().getHeight());
            contentValues.put(IDatabase.IProductTable.WAREHOUSE_LATITUDE, productInfo.getWarehouseLocation().getLatitude());
            contentValues.put(IDatabase.IProductTable.WAREHOUSE_LONGITUDE, productInfo.getWarehouseLocation().getLongitude());

            // Insert data
            sDatabase.insert(IDatabase.IProductTable.TABLE_NAME, null, contentValues);
        }
    }

    /**
     * This method is called to get every data from database table
     *
     * @return cursor containing product list
     */
    public Cursor readProducts() {
        String[] projection = {
                IDatabase.IProductTable.PRODUCT_NAME,
                IDatabase.IProductTable.PRODUCT_DESCRIPTION,
                IDatabase.IProductTable.PRODUCT_IMAGE,
                IDatabase.IProductTable.PRODUCT_PHONE,
                IDatabase.IProductTable.PRODUCT_WEB,
                IDatabase.IProductTable.PRODUCT_PRICE,
                IDatabase.IProductTable.PRODUCT_LENGTH,
                IDatabase.IProductTable.PRODUCT_WIDTH,
                IDatabase.IProductTable.PRODUCT_HEIGHT,
                IDatabase.IProductTable.WAREHOUSE_LONGITUDE,
                IDatabase.IProductTable.WAREHOUSE_LATITUDE};
        return sDatabase.query(
                IDatabase.IProductTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
    }
}
