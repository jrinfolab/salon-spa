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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;


import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


import com.jrinfolab.beautyshop.Constant;

public class DbProvider {

    private static final String TAG = Constant.APP_TAG + "DbProvider";

    private static DbProvider mDbProvider;

    private static DataBaseHelper mDbHelper;
    private static SQLiteDatabase mSqlDb;

    static String PROVIDER_NAME;
    static String PROVIDER_PREFIX;

    public static final String DB_NAME = "salonspadb";
    public static final int DB_VERSION = 1;

    // Table names
    public static final String TABLE_NAME_EMPLOYEE = "employee";

    // Content URI
    public static Uri CONTENT_URI_EMPLOYEE;

    // Employee table columns
    public static final String COL_EMP_ID = "id";
    public static final String COL_EMP_NAME = "name";
    public static final String COL_EMP_PHONE = "phone";
    public static final String COL_EMP_DOB = "dob";
    public static final String COL_EMP_YOE = "experience";
    public static final String COL_EMP_ADDRESS = "address";
    public static final String COL_EMP_IMAGE_URL = "image_url";
    public static final String COL_EMP_IMAGE_BLOB = "image_blob";

    static String CREATE_TABLE_EMPLOYEE = "CREATE TABLE " + TABLE_NAME_EMPLOYEE + " ("
            + COL_EMP_ID         + " TEXT              , "
            + COL_EMP_NAME       + " TEXT              , "
            + COL_EMP_PHONE      + " TEXT              , "
            + COL_EMP_DOB        + " TEXT              , "
            + COL_EMP_YOE        + " INTEGER DEFAULT 0 , "
            + COL_EMP_ADDRESS    + " TEXT              , "
            + COL_EMP_IMAGE_URL  + " TEXT              , "
            + COL_EMP_IMAGE_BLOB + " BLOB              ) ";

/*

    static String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_NAME_GROUPS + " ("
            + COL_GROUP_ID + " TEXT PRIMARY KEY, "
            + COL_GROUP_NAME + " TEXT            , "
            + COL_GROUP_ADMIN_ID + " TEXT            , "
            + COL_GROUP_PHOTO_URL + " TEXT            ) ";

    static String CREATE_GROUP_MEMBERS_TABLE = "CREATE TABLE " + TABLE_NAME_GROUP_MEMBERS + " ("
            + COL_GROUP_ID + " TEXT                                , "
            + COL_M_ID + " TEXT                                , "

            + "PRIMARY KEY("
            + COL_GROUP_ID + ", "
            + COL_M_ID + ") ON CONFLICT REPLACE)";

    static String CREATE_NICK_NAMES_TABLE = "CREATE TABLE " + TABLE_NICK_NAMES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_M_USER_ID + " TEXT                             , "
            + COL_M_WEARABLE_ID + " TEXT                             , "
            + COL_NICK_NAME + " TEXT                             ) ";

    static String CREATE_KIDS_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME_KIDS_CONTACTS + " ("
            + COL_ID + " TEXT UNIQUE NOT NULL , "
            + COL_CONTACT_DEVICE_ID + " TEXT NOT NULL        , "
            + COL_CONTACT_NAME + " TEXT UNIQUE NOT NULL , "
            + COL_CONTACT_NUMBER + " TEXT UNIQUE NOT NULL , "
            + COL_CONTACT_TYPE + " TEXT                 , "
            + COL_CONTACT_PRIORITY + " INTEGER NOT NULL     ) ";


    static String CREATE_SAFE_ZONE_TABLE = "CREATE TABLE " + TABLE_NAME_SAFE_ZONES + " ("
            + COL_M_DEVICE_ID + " TEXT NOT NULL           , "
            + COL_SZ_ID + " TEXT PRIMARY KEY NOT NULL , "
            + COL_SZ_TYPE + " TEXT                      , "
            + COL_ADDRESS + " TEXT                      , "
            + COL_NAME + " TEXT                      , "
            + COL_LATITUDE + " DOUBLE                    , "
            + COL_LONGITUDE + " DOUBLE                    , "
            + COL_RADIUS + " DOUBLE                    ) ";

    static String CREATE_SMART_MESSAGING_TABLE = "CREATE TABLE " + TABLE_NAME_SMART_MESSAGING + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE_ID + " TEXT UNIQUE NOT NULL             , "
            + FILE_ID + " TEXT                             , "
            + FILE_TYPE + " TEXT NOT NULL                    , "
            + FILE_PATH + " TEXT                             , "
            + FROM_USER_ID + " TEXT NOT NULL                    , "
            + TO_USER_ID + " TEXT NOT NULL                    , "
            + TIME + " TEXT NOT NULL                    , "
            + MESSAGE_TEXT + " TEXT                             , "
            + STATUS + " INTEGER                          , "
            + FILE_SIZE + " INTEGER                          , "
            + FILE_DURATION + " TEXT                             ) ";

    static String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_NAME_IMAGES + " ("
            + COL_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + COL_IMAGE_URL + " TEXT                              , "
            + COL_IMAGE_BLOB + " BLOB                              ) ";

    static String CREATE_EVENT_HISTORY_TABLE = "CREATE TABLE " + TABLE_NAME_EVENT_HISTORY + " ("
            + COL_TIMESTAMP + " INTEGER             DEFAULT (strftime('%s','now')), "
            + COL_M_ID + " TEXT    NOT NULL                                  , "
            + COL_M_NAME + " TEXT                                              , "
            + COL_TIMEZONE + " TEXT                                              , "
            + COL_CATEGORY + " TEXT    NOT NULL    DEFAULT 'event'               , "
            + COL_TYPE + " TEXT    NOT NULL    DEFAULT 'other'               , "
            + COL_LATITUDE + " DOUBLE                                            , "
            + COL_LONGITUDE + " DOUBLE                                            , "
            + COL_MESSAGE + " TEXT                                              , "
            + COL_ACCURACY + " DOUBLE              DEFAULT 0.0                   , "
            + COL_BATTERY + " INTEGER             DEFAULT 0                     , "
            + COL_SIGNAL + " INTEGER             DEFAULT -255                  , "
            + COL_CHARGING + " INTEGER             DEFAULT -1                    , "
            + COL_GROUP_ID + " TEXT    NOT NULL                                  , "
            + COL_DURATION + " TEXT    NOT NULL    DEFAULT '0hrs'                , "
            + COL_SEEN + " INTEGER NOT NULL    DEFAULT 0                     , "
            + COL_EX_DATA1 + " TEXT                                              , "
            + COL_EX_DATA2 + " TEXT                                              , "

            + "PRIMARY KEY("
            + COL_TIMESTAMP + ", "
            + COL_TYPE + ", "
            + COL_M_ID + ") ON CONFLICT REPLACE)";

    static String CREATE_CONFIGURATION_TABLE = "CREATE TABLE " + TABLE_NAME_CONFIGURATION + " ("
            + COL_M_DEVICE_ID + " TEXT PRIMARY KEY                         , "
            + COL_SW_VERSION + " TEXT NOT NULL DEFAULT 'UNKNOWN'          , "
            + COL_SW_SYNC_TIME + " INTEGER DEFAULT (strftime('%s','now'))   , "  // last checked time for update
            + COL_SW_SYNC_STATUS + " TEXT    DEFAULT 'in_sync'                , "
            + COL_CONFIG_REP_VERSION + " TEXT                                     , "
            + COL_CONFIG_DES_VERSION + " TEXT                                     , "
            + COL_CONFIG_REP_INTERVAL + " INTEGER DEFAULT 300                      , "  // Seconds
            + COL_CONFIG_DES_INTERVAL + " INTEGER DEFAULT 300                      , "  // Seconds
            + COL_CONFIG_REP_DAYS + " TEXT DEFAULT '1.1.1.1.1.1.1'             , "
            + COL_CONFIG_DES_DAYS + " TEXT DEFAULT '1.1.1.1.1.1.1'             , "
            + COL_CONFIG_REP_START_TIME + " TEXT  DEFAULT '00:00'                  , "
            + COL_CONFIG_DES_START_TIME + " TEXT  DEFAULT '00:00'                  , "
            + COL_CONFIG_REP_END_TIME + " TEXT  DEFAULT '23:59'                  , "
            + COL_CONFIG_DES_END_TIME + " TEXT  DEFAULT '23:59'                  ) ";

*/

    private DbProvider() {
        // private constructor of Singleton class
    }


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

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableNameFromUri(uri);
        return mSqlDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableNameFromUri(uri);
        return mSqlDb.update(tableName, values, selection, selectionArgs);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableNameFromUri(uri);
        return mSqlDb.delete(tableName, selection, selectionArgs);
    }

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DataBaseHelper extends SQLiteOpenHelper {

        private Context context;

        public DataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_EMPLOYEE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private String getTableNameFromUri(Uri uri) {
        if (CONTENT_URI_EMPLOYEE.equals(uri)) return TABLE_NAME_EMPLOYEE;
        return null;
    }
}
