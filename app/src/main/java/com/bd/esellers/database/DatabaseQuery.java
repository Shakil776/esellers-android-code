/*
 * Copyright (c) 2019. $1$1$1. All Rights Reserved.
 *  <p>
 *  Save to the extent permitted by law, you may not use, copy, modify, distribute or create derivative works of this material  or any part of it without the prior written consent of $1$1$1.
 *  <p>
 *  The above Copyright notice  and the permission notice shall be included in all copies or substantial portions of the Software.
 */

package com.bd.esellers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bd.esellers.category.categoryModel.Category;
import com.bd.esellers.mainApp.dataModel.SliderImage;
import com.bd.esellers.utility.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DatabaseQuery {
    private DatabaseOpenHelper helper;
    private SQLiteDatabase db;
    private Context context;

    public DatabaseQuery(Context context) {
        helper = new DatabaseOpenHelper(context);
        this.context=context;
    }

    private void open() {
        db = helper.getWritableDatabase();
    }

    private void close() {
        db.close();
    }

    public void insertSlider(SliderImage slider) {

        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.SLIDER_ID, slider.getId());
        values.put(DatabaseOpenHelper.SLIDER_URL, slider.getSliderImage());
        values.put(DatabaseOpenHelper.SLIDER_STATUS, slider.getPublicationStatus());
        values.put(DatabaseOpenHelper.SLIDER_CREATED_AT, slider.getCreatedAt());
        values.put(DatabaseOpenHelper.SLIDER_UPDATED_AT, slider.getUpdatedAt());
        values.put(DatabaseOpenHelper.SLIDER_PHOTO, Utils.getImageBOA2(slider.getBitmap()));
        try {
            db.insert(DatabaseOpenHelper.SLIDER_TBL, null, values);
        } catch (Exception e) {
            Log.e("DatabaseQuery", "insertData: " + e.getMessage());
        }

        this.close();
    }

    public void insertCategory(Category category){
        this.open();
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.CATEGORY_ID, category.getId());
        values.put(DatabaseOpenHelper.CATEGORY_NAME, category.getCategoryName());
        values.put(DatabaseOpenHelper.CATEGORY_URL, category.getCategoryIcon());
        values.put(DatabaseOpenHelper.CATEGORY_STATUS, category.getPublicationStatus());
        values.put(DatabaseOpenHelper.CATEGORY_PHOTO, Utils.getImageBOA2(category.getBitmap()));
        try {
            db.insert(DatabaseOpenHelper.CATEGORY_TBL, null, values);
        } catch (Exception e) {
            Log.e("DatabaseQuery", "insertCategory: " + e.getMessage());
        }

        this.close();
    }


    public List<Category> getCategory(){
        this.open();
        List<Category> categories = new ArrayList<>();
        Cursor cursor = db.rawQuery(DatabaseOpenHelper.READ_CAT_TBL, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.CATEGORY_ID)));
                category.setCategoryName(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.CATEGORY_NAME)));
                category.setUrl(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.CATEGORY_URL)));
                category.setPublicationStatus(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.CATEGORY_STATUS)));
                category.setBitmap(Utils.getImageBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseOpenHelper.CATEGORY_PHOTO))));
                categories.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        this.close();
        return categories;
    }

    public List<SliderImage> getSlider() {
        this.open();
        List<SliderImage> sliders = new ArrayList<>();
        Cursor cursor = db.rawQuery(DatabaseOpenHelper.READ_SLIDER_TBL, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SliderImage slider = new SliderImage();
                slider.setId(cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_ID)));
                slider.setSliderImage(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_URL)));
                slider.setPublicationStatus(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_STATUS)));
                slider.setCreatedAt(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_CREATED_AT)));
                slider.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_UPDATED_AT)));
                slider.setBitmap(Utils.getImageBitmap(cursor.getBlob(cursor.getColumnIndex(DatabaseOpenHelper.SLIDER_PHOTO))));
                sliders.add(slider);
            } while (cursor.moveToNext());
            cursor.close();
        }
        this.close();
        return sliders;
    }


    public long getSliderCount() {
        this.open();
        long count = DatabaseUtils.queryNumEntries(db, DatabaseOpenHelper.SLIDER_TBL);
        this.close();
        return count;
    }

    public long getCatIconCount() {
        this.open();
        long count = DatabaseUtils.queryNumEntries(db, DatabaseOpenHelper.CATEGORY_TBL);
        this.close();
        return count;
    }

    public void deleteSlider(){
        this.open();
        db.execSQL(DatabaseOpenHelper.DELETE_SLIDER_TABLE);
        this.close();
    }

    public void deleteCat(){
        this.open();
        db.execSQL(DatabaseOpenHelper.DELETE_CAT_TABLE);
        this.close();
    }

    public boolean doesDatabaseExist() {
        File dbFile = context.getDatabasePath(DatabaseOpenHelper.DATABASE_NAME);
        return dbFile.exists();
    }
}
