/*
 * Copyright (c) 2019. $1$1$1. All Rights Reserved.
 *  <p>
 *  Save to the extent permitted by law, you may not use, copy, modify, distribute or create derivative works of this material  or any part of it without the prior written consent of $1$1$1.
 *  <p>
 *  The above Copyright notice  and the permission notice shall be included in all copies or substantial portions of the Software.
 */

package com.bd.esellers.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AppCache.db";
    public static final int DATABASE_VERSION = 1;

    //Slider table
    public static final String SLIDER_TBL = "Slider";
    public static final String SLIDER_ID = "id";
    public static final String SLIDER_URL = "url";
    public static final String SLIDER_STATUS = "status";
    public static final String SLIDER_CREATED_AT = "created_at";
    public static final String SLIDER_UPDATED_AT = "updated_at";
    public static final String SLIDER_PHOTO = "photo";

    public static final String CREATE_SLIDER_TABLE = "CREATE TABLE IF NOT EXISTS " + SLIDER_TBL + " ( " +
            SLIDER_ID + " TEXT, " +
            SLIDER_URL + " TEXT, " +
            SLIDER_STATUS + " TEXT, " +
            SLIDER_CREATED_AT + " TEXT, " +
            SLIDER_UPDATED_AT + " TEXT, " +
            SLIDER_PHOTO + " BLOB );";

    public static final String READ_SLIDER_TBL = "SELECT * FROM " + SLIDER_TBL;

    public static final String DROP_SLIDER_TABLE = "DROP TABLE IF EXISTS " + SLIDER_TBL;

    public static final String DELETE_SLIDER_TABLE = "DELETE FROM " + SLIDER_TBL;


    //Category Table
    public static final String CATEGORY_TBL = "Category";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_URL = "url";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_PHOTO = "photo";

    public static final String CREATE_CAT_TABLE = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TBL + " ( " +
            CATEGORY_ID + " INTEGER, " +
            CATEGORY_NAME + " TEXT, " +
            CATEGORY_URL + " TEXT, " +
            CATEGORY_STATUS + " TEXT, " +
            CATEGORY_PHOTO + " BLOB );";

    public static final String READ_CAT_TBL = "SELECT * FROM " + CATEGORY_TBL;

    public static final String DROP_CAT_TABLE = "DROP TABLE IF EXISTS " + CATEGORY_TBL;

    public static final String DELETE_CAT_TABLE = "DELETE FROM " + CATEGORY_TBL;




    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_SLIDER_TABLE);
            db.execSQL(CREATE_CAT_TABLE);
        }catch (SQLException ex){
            Log.e("DatabaseOpenHelper", "onCreate: "+ex.getMessage() );
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SLIDER_TABLE);
        db.execSQL(DROP_CAT_TABLE);
        onCreate(db);
    }
}
