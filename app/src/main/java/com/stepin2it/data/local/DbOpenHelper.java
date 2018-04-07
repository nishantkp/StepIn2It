package com.stepin2it.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stepin2it.utils.IAppConfig;

public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context) {
        super(context, IAppConfig.DATABASE_NAME, null, IAppConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Execute pure SQL statement to create a new table
        sqLiteDatabase.execSQL(IDatabase.IProductTable.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(IDatabase.ITagsTable.SQL_CREATE_ENTRIES);
        sqLiteDatabase.execSQL(IDatabase.IImagesTable.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > 2) {
            sqLiteDatabase.execSQL(IDatabase.IProductTable.SQL_DELETE_ENTRIES);
            onCreate(sqLiteDatabase);
        }
    }
}
