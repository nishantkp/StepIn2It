package com.stepin2it.data.local;

import android.content.ContentValues;
import android.content.Context;
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
            // Insert data
            sDatabase.insert(IDatabase.IProductTable.TABLE_NAME, null, contentValues);
        }
    }
}