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
import android.util.Log;

public class UgsDBProvider extends ContentProvider {

    private final String TAG = "UgsDBProvider";

    private UGSDataBaseHelper mDbHelper;
    private SQLiteDatabase mSqlDb;

    protected static final String PROVIDER_NAME = "UgsDBProvider";
    protected static String PROVIDER_PREFIX  = "content://" + PROVIDER_NAME + "/";

    private static final String DB_NAME         = "ugsdatabase";
    private static final int DB_VERSION         = 2;
    public static final int SMART_MESSAGE_MAX_LIMIT =  50;
    private static final int SMART_MESSAGE_DELTA_INSERT = 10;
    // This should be sum of SMART_MESSAGE_MAX_LIMIT + SMART_MESSAGE_DELTA_INSERT
    private static final int SMART_MESSAGE_TRIGGER_LIMIT = 60;
    private static final String FILE_TYPE_AUDIO = "'13'";

    // Table names
    public static final String TABLE_NAME_MEMBERS       = "members";
    public static final String TABLE_NAME_GROUPS        = "groups";
    public static final String TABLE_NAME_GROUP_MEMBERS = "group_members";
    public static final String TABLE_NAME_IMAGES        = "images";
    public static final String TABLE_NAME_SAFEZONE      = "safezone";
    public static final String TABLE_NAME_SMART_MESSAGE = "smart_message";

    // Triggers for smart_message
    public static final String TRIGGER_NAME_SMART_MESSAGE = "smart_message_limit_trigger";
    public static final String TRIGGER_NAME_SMART_MESSAGE_FILECLEANUP = "smart_message_file_cleanup";

    /********************************************/
                 /* START Content URI */
    /********************************************/

    // Members URI
    public static Uri MEMBERS_CONTENT_URI       = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_MEMBERS);

    // Groups URI
    public static Uri GROUPS_CONTENT_URI        = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_GROUPS);

    // Group Members URI
    public static Uri GROUP_MEMBERS_CONTENT_URI = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_GROUP_MEMBERS);

    // Images URI
    public static Uri IMAGES_CONTENT_URI        = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_IMAGES);

    // Safezone Content URI
    public static Uri SAFEZONE_CONTENT_URI      = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_SAFEZONE);

    // Smart Message Content URI
    public static Uri SMART_MESSAGE_CONTENT_URI = Uri.parse(PROVIDER_PREFIX + TABLE_NAME_SMART_MESSAGE);

    /********************************************/
                 /* END Content URI */
    /********************************************/

    // Members table columns
    public static final String COL_ID           = "_id";
    public static final String COL_M_ID         = "m_id";
    public static final String COL_M_DEVICE_ID  = "m_device_id";
    public static final String COL_M_PARENT_ID  = "m_parent_id";
    public static final String COL_M_EMAIL      = "m_email";
    public static final String COL_M_TYPE       = "m_type"; /* watch or supervisor or other? */
    public static final String COL_M_NAME       = "m_name";
    public static final String COL_M_PHONE      = "m_phone";
    public static final String COL_M_MAC        = "m_mac";
    public static final String COL_M_STATUS     = "m_status";
    public static final String COL_M_MODEL      = "m_model";
    public static final String COL_M_AGE        = "m_age";
    public static final String COL_M_GENDER     = "m_gender";
    public static final String COL_M_TOKEN      = "m_token";
    public static final String COL_M_PHOTO_URL  = "m_photo_url";
    public static final String COL_M_PARENT_EMAIL = "m_parent_email";
    public static final String COL_M_PARENT_PHONE = "m_parent_phone";
    public static final String COL_M_PARENT_NAME  = "m_parent_name";
    public static final String COL_M_PARENT_PHOTO_URL  = "m_parent_photo_url";

    // Group table columns
    public static final String COL_GROUP_ID        = "group_id";
    public static final String COL_GROUP_NAME      = "group_name";
    public static final String COL_GROUP_ADMIN_ID  = "group_admin_id";
    public static final String COL_GROUP_PHOTO_URL = "group_photo_url";

    // Images table columns
    public static final String COL_IMAGE_ID   = "_id";
    public static final String COL_IMAGE_URL  = "image_url";
    public static final String COL_IMAGE_BLOB = "image_blob";

    // Safezone table columns
    public static final String COL_SZ_ID       = "sz_id";
    public static final String COL_ADDRESS     = "address";
    public static final String COL_NAME        = "name";
    public static final String COL_LATITUDE    = "latitude";
    public static final String COL_LONGITUDE   = "longitude";
    public static final String COL_RADIUS      = "radius";

    // Smart message table columns
    public static final String MESSAGE_ID   = "message_id";
    public static final String FILE_ID      = "file_id";
    public static final String FILE_TYPE    = "file_type";
    public static final String FILE_PATH    = "file_path";
    public static final String FROM_USER_ID = "from_user_id";
    public static final String TO_USER_ID   = "to_user_id";
    public static final String TIME         = "time";
    public static final String MESSAGE_TEXT = "message_text";
    public static final String STATUS       = "status";
    public static final String FILE_SIZE     = "file_size";
    public static final String FILE_DURATION = "file_duration";

    static String CREATE_MEMBERS_TABLE = "CREATE TABLE " + TABLE_NAME_MEMBERS + " ("
            + COL_M_ID          + " TEXT PRIMARY KEY, "
            + COL_M_DEVICE_ID   + " TEXT            , "
            + COL_M_PARENT_ID   + " TEXT            , "
            + COL_M_EMAIL       + " TEXT            , "
            + COL_M_TYPE        + " TEXT NOT NULL   ,"
            + COL_M_NAME        + " TEXT            , "
            + COL_M_PHONE       + " TEXT            , "
            + COL_M_MAC         + " TEXT            , "
            + COL_M_STATUS      + " TEXT            , "
            + COL_M_MODEL       + " TEXT            , "
            + COL_M_AGE         + " TEXT            , "
            + COL_M_GENDER      + " TEXT            , "
            + COL_M_TOKEN       + " TEXT            , "
            + COL_M_PHOTO_URL   + " TEXT            , "
            + COL_M_PARENT_EMAIL + " TEXT           , "
            + COL_M_PARENT_PHONE + " TEXT           , "
            + COL_M_PARENT_NAME  + " TEXT           , "
            + COL_M_PARENT_PHOTO_URL  + " TEXT      ) ";

    static String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_NAME_GROUPS + " ("
            + COL_GROUP_ID          + " TEXT PRIMARY KEY, "
            + COL_GROUP_NAME        + " TEXT            , "
            + COL_GROUP_ADMIN_ID    + " TEXT            , "
            + COL_GROUP_PHOTO_URL   + " TEXT            ) ";

    static String CREATE_GROUP_MEMBERS_TABLE = "CREATE TABLE " + TABLE_NAME_GROUP_MEMBERS + " ("
            + COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT   , "
            + COL_GROUP_ID  + " TEXT                                , "
            + COL_M_ID      + " TEXT UNIQUE NOT NULL                ) ";

    static String CREATE_IMAGES_TABLE = "CREATE TABLE " + TABLE_NAME_IMAGES + " ("
            + COL_IMAGE_ID   +  " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + COL_IMAGE_URL  +  " TEXT                              , "
            + COL_IMAGE_BLOB +  " BLOB                              ) ";

    static String CREATE_SAFEZONE_TABLE = "CREATE TABLE " + TABLE_NAME_SAFEZONE  + " ("
            + COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_SZ_ID       + " TEXT                             , "
            + COL_ADDRESS     + " TEXT                             , "
            + COL_NAME        + " TEXT                             , "
            + COL_LATITUDE    + " DOUBLE                           , "
            + COL_LONGITUDE   + " DOUBLE                           , "
            + COL_RADIUS      + " INTEGER                          ) ";

    static String CREATE_SMART_MESSAGE_TABLE = "CREATE TABLE " + TABLE_NAME_SMART_MESSAGE + " ("
            + COL_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE_ID    + " TEXT UNIQUE NOT NULL             , "
            + FILE_ID       + " TEXT                             , "
            + FILE_TYPE     + " TEXT NOT NULL                    , "
            + FILE_PATH     + " TEXT                             , "
            + FROM_USER_ID  + " TEXT NOT NULL                    , "
            + TO_USER_ID    + " TEXT NOT NULL                    , "
            + TIME          + " TEXT NOT NULL                    , "
            + MESSAGE_TEXT  + " TEXT                             , "
            + STATUS        + " INTEGER                          , "
            + FILE_SIZE     + " INTEGER                          , "
            + FILE_DURATION + " TEXT                             ) ";


    //Trigger statement for SMART_MESSGAGE table to manage db tbl limits
    static String CREATE_SMART_MESSAGE_TRIGGER = "CREATE TRIGGER "
            + TRIGGER_NAME_SMART_MESSAGE + " AFTER INSERT ON "
            + TABLE_NAME_SMART_MESSAGE + " BEGIN DELETE FROM "
            + TABLE_NAME_SMART_MESSAGE + " WHERE "
            + COL_ID + " NOT IN (SELECT "
            + COL_ID + " FROM "
            + TABLE_NAME_SMART_MESSAGE + " ORDER BY "
            + COL_ID + " DESC LIMIT " + SMART_MESSAGE_MAX_LIMIT + " ) AND (SELECT COUNT(*) FROM "
            + TABLE_NAME_SMART_MESSAGE + ") >= " + SMART_MESSAGE_TRIGGER_LIMIT + "; END ";


    //Trigger statement for SMART_MESSGAGE table to cleanup the stale audio files after deletion
    static String CREATE_SMART_MESSAGE_FILECLEANUP = "CREATE TRIGGER "
           + TRIGGER_NAME_SMART_MESSAGE_FILECLEANUP + " DELETE ON "
           + TABLE_NAME_SMART_MESSAGE + " BEGIN SELECT _DELETE_FILE(old."
           + FILE_PATH + ") WHERE old."
           + FILE_TYPE + "="
           + FILE_TYPE_AUDIO + "; END ";


    @Override
    public boolean onCreate() {

        Context context = getContext();
        mDbHelper = new UGSDataBaseHelper(context);

        /**
         * Create a write able database which will trigger its creation if it
         * doesn't already exist.
         */
        mSqlDb = mDbHelper.getWritableDatabase();
        return (mSqlDb == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newUri = null;

        if (MEMBERS_CONTENT_URI.equals(uri)) {

            newUri = insert(TABLE_NAME_MEMBERS, uri, values);

        } else if (GROUPS_CONTENT_URI.equals(uri)) {

            newUri = insert(TABLE_NAME_GROUPS, uri, values);

        } else if (GROUP_MEMBERS_CONTENT_URI.equals(uri)) {

            newUri = insert(TABLE_NAME_GROUP_MEMBERS, uri, values);

        } else if (IMAGES_CONTENT_URI.equals(uri)) {

            newUri = insert(TABLE_NAME_IMAGES, uri, values);

        }  else if (SAFEZONE_CONTENT_URI.equals(uri)) {

            newUri = insert(TABLE_NAME_SAFEZONE, uri, values);

        } else if (SMART_MESSAGE_CONTENT_URI.equals(uri)) {
            try {
                newUri = insert(TABLE_NAME_SMART_MESSAGE, uri, values, SQLiteDatabase.CONFLICT_IGNORE);
            } catch (SQLiteConstraintException e) {
                Log.d(TAG, "Conflict/Error insert into messaging table: "
                        + values.getAsString(MESSAGE_ID));
            }
        } else {
            throw new SQLiteConstraintException("Error in insertion");
        }

        return newUri;

    }

    private Uri insert(String tableName, Uri uri, ContentValues values) {
        return insert(tableName, uri, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private Uri insert(String tableName, Uri uri, ContentValues values, int conflict) {

        long insertedRow = 0;

        insertedRow = mSqlDb.insertWithOnConflict(tableName, null, values,
                conflict);

        if (insertedRow > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, insertedRow);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        throw new SQLiteConstraintException("Error while insertion");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;

        if (MEMBERS_CONTENT_URI.equals(uri)) {

            cursor = query(TABLE_NAME_MEMBERS, uri, projection, selection, selectionArgs, sortOrder);

        } else if (GROUPS_CONTENT_URI.equals(uri)) {

            cursor = query(TABLE_NAME_GROUPS, uri, projection, selection, selectionArgs, sortOrder);

        } else if (GROUP_MEMBERS_CONTENT_URI.equals(uri)) {

            cursor = query(TABLE_NAME_GROUP_MEMBERS, uri, projection, selection, selectionArgs, sortOrder);

        } else if (IMAGES_CONTENT_URI.equals(uri)) {

            cursor = query(TABLE_NAME_IMAGES, uri, projection, selection, selectionArgs, sortOrder);

        } else if (SAFEZONE_CONTENT_URI.equals(uri)) {

            cursor = query(TABLE_NAME_SAFEZONE, uri, projection, selection, selectionArgs, sortOrder);

        } else if (SMART_MESSAGE_CONTENT_URI.equals(uri)) {

            cursor = query(TABLE_NAME_SMART_MESSAGE, uri, projection, selection, selectionArgs, sortOrder);

        }

        return cursor;
    }

    private Cursor query(String tableName, Uri uri, String[] projection, String selection,
                         String[] selectionArgs, String sortOrder) {

        return mSqlDb.query(tableName, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int rows = 0;

        if (MEMBERS_CONTENT_URI.equals(uri)) {

            rows = update(TABLE_NAME_MEMBERS, uri, values, selection, selectionArgs);

        } else if (GROUPS_CONTENT_URI.equals(uri)) {

            rows = update(TABLE_NAME_GROUPS, uri, values, selection, selectionArgs);

        } else if (GROUP_MEMBERS_CONTENT_URI.equals(uri)) {

            rows = update(TABLE_NAME_GROUP_MEMBERS, uri, values, selection, selectionArgs);

        } else if (IMAGES_CONTENT_URI.equals(uri)) {

            rows = update(TABLE_NAME_IMAGES, uri, values, selection, selectionArgs);

        } if (SAFEZONE_CONTENT_URI.equals(uri)) {

            rows = update(TABLE_NAME_SAFEZONE, uri, values, selection, selectionArgs);

        } else if (SMART_MESSAGE_CONTENT_URI.equals(uri)) {

            rows = update(TABLE_NAME_SMART_MESSAGE, uri, values, selection, selectionArgs);
        }

        return rows;
    }

    private int update(String tableName, Uri uri, ContentValues values, String selection,
                       String[] selectionArgs) {
        int rows = 0;

        rows = mSqlDb.update(tableName, values, selection,
                selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return rows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rows = 0;

        if (MEMBERS_CONTENT_URI.equals(uri)) {

            rows = delete(TABLE_NAME_MEMBERS, selection, selectionArgs);

        } else if (GROUPS_CONTENT_URI.equals(uri)) {

            rows = delete(TABLE_NAME_GROUPS, selection, selectionArgs);

        } else if (GROUP_MEMBERS_CONTENT_URI.equals(uri)) {

            rows = delete(TABLE_NAME_GROUP_MEMBERS, selection, selectionArgs);

        } else if (IMAGES_CONTENT_URI.equals(uri)) {

            rows = delete(TABLE_NAME_IMAGES, selection, selectionArgs);

        } if (SAFEZONE_CONTENT_URI.equals(uri)) {

            rows = delete(TABLE_NAME_SAFEZONE, selection, selectionArgs);

        } else if (SMART_MESSAGE_CONTENT_URI.equals(uri)) {

            rows = delete(TABLE_NAME_SMART_MESSAGE, selection, selectionArgs);
        }

        return rows;
    }

    private int delete(String tableName, String selection, String[] selectionArgs) {

        return mSqlDb.delete(tableName, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private class UGSDataBaseHelper extends SQLiteOpenHelper {
        public UGSDataBaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MEMBERS_TABLE);
            db.execSQL(CREATE_GROUPS_TABLE);
            db.execSQL(CREATE_GROUP_MEMBERS_TABLE);
            db.execSQL(CREATE_IMAGES_TABLE);
            db.execSQL(CREATE_SAFEZONE_TABLE);
            db.execSQL(CREATE_SMART_MESSAGE_TABLE);
            db.execSQL(CREATE_SMART_MESSAGE_TRIGGER);
            db.execSQL(CREATE_SMART_MESSAGE_FILECLEANUP);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO: save previous data before dropping table
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MEMBERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUPS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GROUP_MEMBERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_IMAGES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SAFEZONE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SMART_MESSAGE);
            onCreate(db);
        }
    }
}
