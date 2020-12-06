/* BORQS Software Solutions Pvt Ltd. CONFIDENTIAL
 * Copyright (c) 2015 All rights reserved.
 *
 * The source code contained or described herein and all documents
 * related to the source code ("Material") are owned by BORQS Software
 * Solutions Pvt Ltd. No part of the Material may be used,copied,
 * reproduced, modified, published, uploaded,posted, transmitted,
 * distributed, or disclosed in any way without BORQS Software
 * Solutions Pvt Ltd. prior written permission.
 *
 * No license under any patent, copyright, trade secret or other
 * intellectual property right is granted to or conferred upon you
 * by disclosure or delivery of the Materials, either expressly, by
 * implication, inducement, estoppel or otherwise. Any license
 * under such intellectual property rights must be express and
 * approved by BORQS Software Solutions Pvt Ltd. in writing.
 */
package com.jrinfolab.beautyshop.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jrinfolab.beautyshop.helper.Constant;

public class DbProvider extends ContentProvider {

    private static final String TAG = Constant.APP_TAG + "DbProvider";

    private static DataBaseHelper mDbHelper;
    private static SQLiteDatabase mSqlDb;

    private static final String DB_NAME         = "salonspa";
    protected static final String PROVIDER_NAME = "salonspa_dbprovider";
    protected static String PROVIDER_PREFIX     = "content://" + PROVIDER_NAME + "/";
    private static final int DB_VERSION         = 1;

    // Table names
    public static final String TABLE_NAME_EMPLOYEE = "employee";
    public static final String TABLE_NAME_BRANCH = "branches";

    // Content URI
    public static Uri CONTENT_URI_EMPLOYEE   = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_EMPLOYEE);
    public static Uri CONTENT_URI_BRANCH     = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_BRANCH);

    // Employee table columns
    public static final String COL_EMP_ID        = "id";
    public static final String COL_EMP_NAME      = "name";
    public static final String COL_EMP_PHONE     = "phone";
    public static final String COL_EMP_YOE       = "experience";
    public static final String COL_EMP_ADDRESS   = "address";
    public static final String COL_EMP_IMAGE_URL = "image_url";
    public static final String COL_EMP_BRANCH_ID = "branch_id";
    public static final String COL_EMP_BRANCH_NAME = "branch_name";

    static String CREATE_TABLE_EMPLOYEE = "CREATE TABLE " + TABLE_NAME_EMPLOYEE + " ("
            + COL_EMP_ID        + " TEXT              , "
            + COL_EMP_NAME      + " TEXT              , "
            + COL_EMP_PHONE     + " TEXT              , "
            + COL_EMP_YOE       + " INTEGER DEFAULT 1 , "
            + COL_EMP_ADDRESS   + " TEXT              , "
            + COL_EMP_IMAGE_URL + " TEXT              ,"
            + COL_EMP_BRANCH_ID + " TEXT              ,"
            + COL_EMP_BRANCH_NAME + " TEXT             )";

    // Branch table columns
    public static final String COL_BRANCH_ID      = "id";
    public static final String COL_BRANCH_NAME    = "name";
    public static final String COL_BRANCH_ADDRESS = "address";
    public static final String COL_BRANCH_PHOTOS  = "photos"; // photo path separated by comma
    public static final String COL_BRANCH_LAT     = "latitude";
    public static final String COL_BRANCH_LNG     = "longitude";

    static String CREATE_TABLE_BRANCH = "CREATE TABLE " + TABLE_NAME_BRANCH + " ("
            + COL_BRANCH_ID       + " TEXT               , "
            + COL_BRANCH_NAME     + " TEXT               , "
            + COL_BRANCH_ADDRESS  + " TEXT               , "
            + COL_BRANCH_PHOTOS   + " TEXT               , "
            + COL_BRANCH_LAT      + " DOUBLE DEFAULT 1.0 , "
            + COL_BRANCH_LNG      + " DOUBLE DEFAULT 1.0 ) ";

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new DataBaseHelper(context);
        mSqlDb = mDbHelper.getWritableDatabase();
        return (mSqlDb == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableNameFromUri(uri);
        return insert(tableName, uri, values);
    }

    private Uri insert(String tableName, Uri uri, ContentValues values) {
        return insert(tableName, uri, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private Uri insert(String tableName, Uri uri, ContentValues values, int conflict) {

        long insertedRow = 0;
        insertedRow = mSqlDb.insertWithOnConflict(tableName, null, values, conflict);

        if (insertedRow > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, insertedRow);
            return newUri;
        }

        throw new SQLiteConstraintException("Error while insertion");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableNameFromUri(uri);
        return mSqlDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableNameFromUri(uri);
        return mSqlDb.update(tableName, values, selection, selectionArgs);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableNameFromUri(uri);
        return mSqlDb.delete(tableName, selection, selectionArgs);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        private Context context;

        public DataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_EMPLOYEE);
            db.execSQL(CREATE_TABLE_BRANCH);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO : Logout
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EMPLOYEE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BRANCH);
            onCreate(db);
        }
    }

    private String getTableNameFromUri(Uri uri) {
        if (CONTENT_URI_EMPLOYEE.equals(uri)) return TABLE_NAME_EMPLOYEE;
        if (CONTENT_URI_BRANCH.equals(uri)) return TABLE_NAME_BRANCH;
        return null;
    }
}
