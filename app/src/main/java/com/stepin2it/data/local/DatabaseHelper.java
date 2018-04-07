package com.stepin2it.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.stepin2it.ui.models.Dimensions;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.ui.models.WarehouseLocation;

import java.util.ArrayList;
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
            contentValues.put(IDatabase.IProductTable.PRODUCT_PHONE, productInfo.getProductPhone());
            contentValues.put(IDatabase.IProductTable.PRODUCT_WEB, productInfo.getProductWebUrl());
            contentValues.put(IDatabase.IProductTable.PRODUCT_PRICE, productInfo.getPrice());
            contentValues.put(IDatabase.IProductTable.PRODUCT_LENGTH, productInfo.getDimensions().getLength());
            contentValues.put(IDatabase.IProductTable.PRODUCT_WIDTH, productInfo.getDimensions().getWidth());
            contentValues.put(IDatabase.IProductTable.PRODUCT_HEIGHT, productInfo.getDimensions().getHeight());
            contentValues.put(IDatabase.IProductTable.WAREHOUSE_LATITUDE, productInfo.getWarehouseLocation().getLatitude());
            contentValues.put(IDatabase.IProductTable.WAREHOUSE_LONGITUDE, productInfo.getWarehouseLocation().getLongitude());
            contentValues.put(IDatabase.IProductTable.PRODUCT_WEIGHT, productInfo.getWeight());
            contentValues.put(IDatabase.IProductTable.PRODUCT_ID, productInfo.getProductId());
            // Insert data
            sDatabase.insert(IDatabase.IProductTable.TABLE_NAME, null, contentValues);
            insertProductTags(productInfo.getTags(), productInfo.getProductId());
            insertProductImages(productInfo.getImageList(), productInfo.getProductId());
        }
    }

    /**
     * Insert product image urls into image-table along with product ids
     *
     * @param imageList ArrayList of image urls
     * @param productId unique product id
     */
    private void insertProductImages(ArrayList<String> imageList, String productId) {
        for (String image : imageList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(IDatabase.IImagesTable.PRODUCT_ID, productId);
            contentValues.put(IDatabase.IImagesTable.PRODUCT_IMAGE, image);
            sDatabase.insert(IDatabase.IImagesTable.TABLE_NAME, null, contentValues);
        }
    }

    /**
     * Insert product tags into tags-table along with product ids
     *
     * @param tags      tags in form of array
     * @param productId unique product id
     */
    private void insertProductTags(String[] tags, String productId) {
        for (String image : tags) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(IDatabase.ITagsTable.PRODUCT_ID, productId);
            contentValues.put(IDatabase.ITagsTable.PRODUCT_TAG, image);
            sDatabase.insert(IDatabase.ITagsTable.TABLE_NAME, null, contentValues);
        }
    }

    /**
     * This method is called to get every data from database table
     *
     * @return product list in List<ProductInfo> format
     */
    public List<ProductInfo> readProducts() {
        String[] projection = {
                IDatabase.IProductTable.PRODUCT_NAME,
                IDatabase.IProductTable.PRODUCT_DESCRIPTION,
                IDatabase.IProductTable.PRODUCT_PHONE,
                IDatabase.IProductTable.PRODUCT_WEB,
                IDatabase.IProductTable.PRODUCT_PRICE,
                IDatabase.IProductTable.PRODUCT_LENGTH,
                IDatabase.IProductTable.PRODUCT_WIDTH,
                IDatabase.IProductTable.PRODUCT_HEIGHT,
                IDatabase.IProductTable.WAREHOUSE_LONGITUDE,
                IDatabase.IProductTable.WAREHOUSE_LATITUDE,
                IDatabase.IProductTable.PRODUCT_ID,
                IDatabase.IProductTable.PRODUCT_WEIGHT};


        Cursor productInfoCursor =
                sDatabase.query(
                        IDatabase.IProductTable.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null);

        List<ProductInfo> productInfoList = new ArrayList<>();
        while (productInfoCursor.moveToNext()) {
            // Get each element from Cursor and create an array list
            String productName =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_NAME));
            String productDescription =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_DESCRIPTION));
            String productPhone =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_PHONE));
            String productWebUrl =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_WEB));
            String productPrice =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_PRICE));
            String productLength =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_LENGTH));
            String productWidth =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_WIDTH));
            String productHeight =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_HEIGHT));
            String productLatitude =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.WAREHOUSE_LATITUDE));
            String productLongitude =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.WAREHOUSE_LONGITUDE));
            String productWeight =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_WEIGHT));
            String productId =
                    productInfoCursor.getString(productInfoCursor.getColumnIndex(IDatabase.IProductTable.PRODUCT_ID));

            // Retrieve the image and tags associated with unique product-ids
            ArrayList<String> imageList = getImageList(productId);
            String[] tags = getProductTags(productId);

            // Add new ProductInfo object into List
            productInfoList.add(new ProductInfo(
                    productName,
                    productDescription,
                    productPhone,
                    productWebUrl,
                    productPrice,
                    tags,
                    new Dimensions(productLength, productWidth, productHeight),
                    new WarehouseLocation(productLatitude, productLongitude),
                    productWeight,
                    productId,
                    imageList));
        }
        return productInfoList;
    }

    /**
     * Get the data from tags-table for unique product-id
     *
     * @param productId product-id(unique)
     * @return String array for product tags
     */
    private String[] getProductTags(String productId) {
        String[] projection = {IDatabase.ITagsTable.PRODUCT_TAG};
        String selection = IDatabase.ITagsTable.PRODUCT_ID + "=?";
        String[] selectionArgs = {productId};
        Cursor cursor =
                sDatabase.query(
                        IDatabase.ITagsTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
        ArrayList<String> tags = new ArrayList<>();
        cursor.moveToFirst();
        do {
            tags.add(cursor.getString(cursor.getColumnIndex(IDatabase.ITagsTable.PRODUCT_TAG)));
        } while (cursor.moveToNext());
        cursor.close();
        String[] tagList = new String[tags.size()];
        return tags.toArray(tagList);
    }

    /**
     * Get the image urls from image-table
     *
     * @param productId product-id(unique)
     * @return ArrayList of image urls
     */
    private ArrayList<String> getImageList(String productId) {
        String[] projection = {IDatabase.IImagesTable.PRODUCT_IMAGE};
        String selection = IDatabase.IImagesTable.PRODUCT_ID + "=?";
        String[] selectionArgs = {productId};
        Cursor cursor =
                sDatabase.query(
                        IDatabase.IImagesTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
        ArrayList<String> imageList = new ArrayList<>();
        cursor.moveToFirst();
        do {
            imageList.add(cursor.getString(cursor.getColumnIndex(IDatabase.IImagesTable.PRODUCT_IMAGE)));
        } while (cursor.moveToNext());
        cursor.close();
        return imageList;
    }
}
