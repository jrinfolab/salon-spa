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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.jrinfolab.beautyshop.helper.Util;
import com.jrinfolab.beautyshop.pojo.Branch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DbHelper {

    private final static String TAG = "DbHelper";
    private final static String EQUAL_QUESTION_MARK = "=?";
    private static final String SYMBOL_OR = " OR ";

    public static Uri addBranch(Context context, Branch branch) {
        ContentValues values = new ContentValues();
        values.put(DbProvider.COL_BRANCH_ID, branch.getId());
        values.put(DbProvider.COL_BRANCH_NAME, branch.getName());
        values.put(DbProvider.COL_BRANCH_ADDRESS, branch.getAddress());
        values.put(DbProvider.COL_BRANCH_LAT, branch.getLat());
        values.put(DbProvider.COL_BRANCH_LNG, branch.getLng());
        values.put(DbProvider.COL_BRANCH_PHOTOS, Util.getString(branch.getPhotoList()));
        Uri uri = context.getContentResolver().insert(DbProvider.CONTENT_URI_BRANCH, values);
        Log.d(TAG, "New branch added : " + uri.toString());
        return uri;
    }

    public static int updateBranch(Context context, Branch branch) {

        String whereClause = DbProvider.COL_BRANCH_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = { branch.getId()};

        ContentValues values = new ContentValues();
        values.put(DbProvider.COL_BRANCH_ID, branch.getId());
        values.put(DbProvider.COL_BRANCH_NAME, branch.getName());
        values.put(DbProvider.COL_BRANCH_ADDRESS, branch.getAddress());
        values.put(DbProvider.COL_BRANCH_LAT, branch.getLat());
        values.put(DbProvider.COL_BRANCH_LNG, branch.getLng());
        values.put(DbProvider.COL_BRANCH_PHOTOS, Util.getString(branch.getPhotoList()));
        int row = context.getContentResolver().update(DbProvider.CONTENT_URI_BRANCH, values, whereClause, whereArgs );
        Log.d(TAG, "Updated row : " + row);
        return row;
    }

    public static int deleteBranch(Context context, String branchId) {
        String whereClause = DbProvider.COL_BRANCH_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = { branchId};
        int row = context.getContentResolver().delete(DbProvider.CONTENT_URI_BRANCH, whereClause, whereArgs );
        Log.d(TAG, "Deleted row : " + row);
        return row;
    }

    public static Branch getBranch(Context context, String branchId) {

        String whereClause = DbProvider.COL_BRANCH_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {branchId};

        Cursor cursor = context.getContentResolver().query(DbProvider.CONTENT_URI_BRANCH,
                null, whereClause, whereArgs, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {

            Branch branch = new Branch();
            branch.setId(getStrVal(cursor, DbProvider.COL_BRANCH_ID));
            branch.setName(getStrVal(cursor, DbProvider.COL_BRANCH_NAME));
            branch.setAddress(getStrVal(cursor, DbProvider.COL_BRANCH_ADDRESS));
            branch.setLat(getDblVal(cursor, DbProvider.COL_BRANCH_LAT));
            branch.setLng(getDblVal(cursor, DbProvider.COL_BRANCH_LNG));
            branch.setPhotoList(Util.getStringArray(getStrVal(cursor, DbProvider.COL_BRANCH_PHOTOS)));

            cursor.close();
            return branch;
        }
        return null;
    }

    public static List<Branch> getBranchList(Context context) {

        Cursor cursor = context.getContentResolver().query(DbProvider.CONTENT_URI_BRANCH,
                null, null, null, null);

        if (cursor == null) {
            return null;
        }

        if (cursor.getCount() > 0) {
            List<Branch> branchList = new ArrayList<Branch>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Branch branch = new Branch();
                branch.setId(getStrVal(cursor, DbProvider.COL_BRANCH_ID));
                branch.setName(getStrVal(cursor, DbProvider.COL_BRANCH_NAME));
                branch.setAddress(getStrVal(cursor, DbProvider.COL_BRANCH_ADDRESS));
                branch.setLat(getDblVal(cursor, DbProvider.COL_BRANCH_LAT));
                branch.setLng(getDblVal(cursor, DbProvider.COL_BRANCH_LNG));
                branch.setPhotoList(Util.getStringArray(getStrVal(cursor, DbProvider.COL_BRANCH_PHOTOS)));
                branchList.add(branch);
                cursor.moveToNext();
            }
            cursor.close();
            return branchList;
        }
        return null;
    }

    private static String getStrVal(Cursor cursor, String key) {
        return cursor.getString(cursor.getColumnIndex(key));
    }

    private static double getDblVal(Cursor cursor, String key) {
        return cursor.getDouble(cursor.getColumnIndex(key));
    }

/*
    public static User getMember(Context context, String user) {

        User superUser = new User();
        String whereClause;
        String[] whereArgs = new String[1];

        if (Constants.USERTYPE_CURRENT_USER.equals(user)) {

            whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
            whereArgs[0] = Constants.USERTYPE_CURRENT_USER;

        } else {

            whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
            whereArgs[0] = user;
        }

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                null, whereClause, whereArgs, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

            try {

                superUser.setId(getValue(cursor, DbProvider.COL_M_ID));
                superUser.setEmail(getValue(cursor, DbProvider.COL_M_EMAIL));
                superUser.setType(getValue(cursor, DbProvider.COL_M_TYPE));
                superUser.setDeviceType(Integer.parseInt(getValue(cursor, DbProvider.COL_M_DEVICE_TYPE)));
                superUser.setDeviceId(getValue(cursor, DbProvider.COL_M_DEVICE_ID));
                superUser.setName(getValue(cursor, DbProvider.COL_M_NAME));
                superUser.setPhone(getValue(cursor, DbProvider.COL_M_PHONE));
                superUser.setMac(getValue(cursor, DbProvider.COL_M_MAC));
                superUser.setStatus(getValue(cursor, DbProvider.COL_M_STATUS));
                superUser.setModel(getValue(cursor, DbProvider.COL_M_MODEL));
                superUser.setParentId(getValue(cursor, DbProvider.COL_M_CREATED_BY));
                superUser.setAge(getValue(cursor, DbProvider.COL_M_AGE));
                superUser.setGender(getValue(cursor, DbProvider.COL_M_GENDER));
                superUser.setToken(getValue(cursor, DbProvider.COL_M_TOKEN));
                superUser.setPhotoUrl(getValue(cursor, DbProvider.COL_M_PHOTO_URL));
                superUser.setParentNickName(getValue(cursor, DbProvider.COL_M_NICK_NAME));

            } catch (SQLiteException e) {
                Log.e(TAG, "Exception : " + e.getMessage());
            } finally {
                cursor.close();
            }
        }

        return superUser;
    }

    // TODO: this method has to be removed after multi-group,
    // TODO :use UgsPreference.getCurrentGroup(mContext) instead to get current logged in group.
    public static String getDefaultGroupId(Context context) {

        String groupId = "";
        String whereClause = DbProvider.COL_GROUP_ADMIN_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {getMemberId(context)};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.GROUPS_CONTENT_URI,
                new String[]{DbProvider.COL_GROUP_ID}, whereClause, whereArgs, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            groupId = cursor.getString(cursor.getColumnIndex(DbProvider.COL_GROUP_ID));
            cursor.close();
        }
        return groupId;
    }

    public static String getMemberId(Context context) {

        String memberId = "";
        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_CURRENT_USER};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_ID}, whereClause, whereArgs, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            memberId = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_ID));
            cursor.close();
        }

        return memberId;
    }

    public static String getParentEmail(Context context) {

        String parentEmail = "";
        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_CURRENT_USER};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_EMAIL}, whereClause, whereArgs, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            parentEmail = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_EMAIL));
            cursor.close();
        }

        return parentEmail;
    }

    public static String getMemberToken(Context context) {

        String token = "";
        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_CURRENT_USER};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_TOKEN}, whereClause, whereArgs, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            token = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_TOKEN));
            cursor.close();
        }

        return token;
    }

    public static ArrayList<User> getAllWearables(Context context) {

        ArrayList<User> userList = new ArrayList();

        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_WATCH};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI, null,
                whereClause, whereArgs, DbProvider.COL_M_ID + " ASC");
        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    User memberDetails = getUserObject(cursor);

                    if (memberDetails != null) {
                        userList.add(memberDetails);
                    }

                    cursor.moveToNext();
                }
            }

            cursor.close();
        }

        return userList;
    }

    public static ContentValues parseMemberValues(Context context, JSONObject json, String ugsToken, byte[] memberImage) {

        ContentValues memberInfo = new ContentValues();

        try {

            memberInfo.put(DbProvider.COL_M_ID, json.getString(ParserConstants.JSONOBJ_USER_ID));
            memberInfo.put(DbProvider.COL_M_NAME, json.getString(ParserConstants.JSONOBJ_USER_NAME));
            memberInfo.put(DbProvider.COL_M_STATUS, json.getString(ParserConstants.JSONOBJ_USER_STATUS));

            String memberPhone = "";
            String memberMac = "";
            String memberModel = "";
            String memberEmail = "";
            String memberPhoto = "";
            String memberAge = "";
            String memberGender = "";
            String memberType = "";
            String deviceId = "";
            String parentId = "";
            String parentNickName = "";
            int deviceType = Constants.DEVICE_TYPE_NONE;

            if (json.has(ParserConstants.JSONOBJ_USER_TYPE)) {

                memberType = json.getString(ParserConstants.JSONOBJ_USER_TYPE);

                if (Constants.USERTYPE_WATCH.equals(memberType)) {
                    deviceType = Constants.DEVICE_TYPE_WATCH;
                }
            }

            if (json.has(ParserConstants.JSONOBJ_MEMBER_PHONE)) {
                memberPhone = json.getString(ParserConstants.JSONOBJ_MEMBER_PHONE);
            }

            if (json.has(ParserConstants.JSONOBJ_USER_AGE)) {
                memberAge = json.getString(ParserConstants.JSONOBJ_USER_AGE);
            }

            if (json.has(ParserConstants.JSONOBJ_USER_GENDER)) {
                memberGender = json.getString(ParserConstants.JSONOBJ_USER_GENDER);
            }

            if (json.has(ParserConstants.JSONOBJ_WEARABLE_MAC)) {
                memberMac = json.getString(ParserConstants.JSONOBJ_WEARABLE_MAC);
            }

            if (json.has(ParserConstants.JSONOBJ_WEARABLE_MODEL)) {

                memberModel = json.getString(ParserConstants.JSONOBJ_WEARABLE_MODEL);

                if (Constants.MODEL_TYPE_PERTRACK.equals(memberModel) ||
                        Constants.MODEL_TYPE_TRACKER.equals(memberModel)) {
                    deviceType = Constants.DEVICE_TYPE_TRACKER;
                } else if (Constants.MODEL_TYPE_WEARABLE.equals(memberModel) ||
                        Constants.MODEL_TYPE_SPRINT.equals(memberModel)) {
                    deviceType = Constants.DEVICE_TYPE_WATCH;
                }
            }

            if (json.has(ParserConstants.JSONOBJ_USER_EMAIL)) {
                memberEmail = json.getString(ParserConstants.JSONOBJ_USER_EMAIL);
            }

            if (json.has(ParserConstants.JSONOBJ_USER_PHOTO_URL)) {
                memberPhoto = json.optString(ParserConstants.JSONOBJ_USER_PHOTO_URL, ParserConstants.DEFAULT_USER_PHOTO_URL);
            } else {
                if (deviceType == Constants.DEVICE_TYPE_NONE){
                    memberPhoto = getUriForResource(context, R.drawable.ic_default_user).toString();
                } else {
                    memberPhoto = getUriForResource(context, R.drawable.ic_default_watch).toString();
                }
            }

            if (json.has(ParserConstants.JSONOBJ_DEVICE_ID)) {
                deviceId = json.getString(ParserConstants.JSONOBJ_DEVICE_ID);
            }

            if (json.has(ParserConstants.JSONOBJ_USER_PARENT_ID)) {
                parentId = json.getString(ParserConstants.JSONOBJ_USER_PARENT_ID);
            }

            if (json.has(Constants.KEY_META_PROFILE)) {
                if (!json.isNull(Constants.KEY_META_PROFILE)) {

                    if (json.getJSONObject(Constants.KEY_META_PROFILE).has(Constants.KEY_NICK_NAME)) {

                        parentNickName =
                                json.getJSONObject(Constants.KEY_META_PROFILE).optString(Constants.KEY_NICK_NAME,"");
                    }
                }

            }

            memberInfo.put(DbProvider.COL_M_PHONE, memberPhone);
            memberInfo.put(DbProvider.COL_M_MAC, memberMac);
            memberInfo.put(DbProvider.COL_M_MODEL, memberModel);
            memberInfo.put(DbProvider.COL_M_EMAIL, memberEmail);
            memberInfo.put(DbProvider.COL_M_PHOTO_URL, memberPhoto);
            memberInfo.put(DbProvider.COL_M_TOKEN, ugsToken);
            memberInfo.put(DbProvider.COL_M_AGE, memberAge);
            memberInfo.put(DbProvider.COL_M_GENDER, memberGender);
            memberInfo.put(DbProvider.COL_M_TYPE, memberType);
            memberInfo.put(DbProvider.COL_M_DEVICE_TYPE, deviceType);
            memberInfo.put(DbProvider.COL_M_DEVICE_ID, deviceId);
            memberInfo.put(DbProvider.COL_M_CREATED_BY, parentId);
            memberInfo.put(DbProvider.COL_M_NICK_NAME, parentNickName);

            if (json.has(ParserConstants.JSON_STRING_CONFIG_SETTINGS)) {
                JSONObject configJson = json.getJSONObject(ParserConstants.JSON_STRING_CONFIG_SETTINGS);
                setConfiguration(context, UgsWebServiceJson.parseConfigInfo(context, deviceId, configJson));
            }

            if (json.has(ParserConstants.JSON_STRING_DEVICE_STATUS)) {
                JSONObject deviceStatus = json.getJSONObject(ParserConstants.JSON_STRING_DEVICE_STATUS);
                if (deviceStatus.has(ParserConstants.JSON_STRING_FOTA_INFO)) {
                    JSONObject fotaInfo = deviceStatus.getJSONObject(ParserConstants.JSON_STRING_FOTA_INFO);
                    setSWInfo(context, UgsWebServiceJson.parseFotaInfo(deviceId, fotaInfo));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return memberInfo;
    }

    public static ContentValues parseGroupValues(Context context, JSONObject json) {

        ContentValues groupInfo = new ContentValues();

        try {

            groupInfo.put(DbProvider.COL_GROUP_NAME,
                    json.getString(ParserConstants.JSONOBJ_USER_GROUP_TITLE));
            String adminId = "";
            String groupPhoto = "";
            String groupId = "";

            if (json.has(ParserConstants.JSONOBJ_USER_GROUP_ID)) {
                groupId = json.getString(ParserConstants.JSONOBJ_USER_GROUP_ID);
            }

            if (json.has(ParserConstants.JSONOBJ_USER_GROUP_PHOTO_URL)) {
                groupPhoto = json.getString(ParserConstants.JSONOBJ_USER_GROUP_PHOTO_URL);
            }

            if (json.has(ParserConstants.JSONOBJ_USER_GROUP_ADMIN)) {
                adminId = json.getString(ParserConstants.JSONOBJ_USER_GROUP_ADMIN);
            } else {
                adminId = getMemberId(context);
            }

            groupInfo.put(DbProvider.COL_GROUP_ADMIN_ID, adminId);
            groupInfo.put(DbProvider.COL_GROUP_PHOTO_URL, groupPhoto);
            groupInfo.put(DbProvider.COL_GROUP_ID, groupId);

            //TODO : Handle null case properly

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return groupInfo;
    }

    public static String getValue(Cursor cursor, String colName) {
        return cursor.getString(cursor.getColumnIndex(colName));
    }

    public static boolean updateUserProfileDataIntoDb(Context context, User user) {

        String name, email, phone, gender, age, nickName;
        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;

        if (user != null) {

            name = user.getName();
            nickName = user.getParentNickName();
            email = user.getEmail();
            phone = user.getPhone();
            gender = user.getGender();
            age = user.getAge();

            String[] whereArgs = {user.getId()};

            ContentValues contentValues = new ContentValues();

            contentValues.put(DbProvider.COL_M_NAME, name);
            contentValues.put(DbProvider.COL_M_NICK_NAME, nickName);
            contentValues.put(DbProvider.COL_M_EMAIL, email);
            contentValues.put(DbProvider.COL_M_PHONE, phone);
            contentValues.put(DbProvider.COL_M_GENDER, gender);
            contentValues.put(DbProvider.COL_M_AGE, age);

            DbProvider.getInstance(context).update(DbProvider.MEMBERS_CONTENT_URI,
                    contentValues, whereClause, whereArgs);
            return true;
        }

        return false;
    }

    public static void updateImageUrl(Context context, String userId, String imageUrl) {

        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {userId};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbProvider.COL_M_PHOTO_URL, imageUrl);

        DbProvider.getInstance(context).update(DbProvider.MEMBERS_CONTENT_URI, contentValues, whereClause, whereArgs);
    }

    public static int clearAppData(Context context) {

        return DbProvider.getInstance(context).delete(
                DbProvider.DROP_ALL_CONTENT_URI, null, null);
    }

    public static boolean removeMember(Context context, String memberId) {

        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {memberId};

        boolean isMemberRemoved = false;

        int deleteFromMemberTable = DbProvider.getInstance(context).delete(DbProvider.MEMBERS_CONTENT_URI,
                whereClause, whereArgs);

        if (deleteFromMemberTable > 0) {

            // Remove user from group member table
            DbProvider.getInstance(context).delete(DbProvider.GROUP_MEMBERS_CONTENT_URI,
                    whereClause, whereArgs);

            isMemberRemoved = true;
        }

        return isMemberRemoved;
    }

    private static User getUserObject(Cursor cursor) {

        User user = new User();

        try {

            user.setId(getValue(cursor, DbProvider.COL_M_ID));
            user.setEmail(getValue(cursor, DbProvider.COL_M_EMAIL));
            user.setType(getValue(cursor, DbProvider.COL_M_TYPE));
            user.setDeviceType(Integer.parseInt(getValue(cursor, DbProvider.COL_M_DEVICE_TYPE)));
            user.setDeviceId(getValue(cursor, DbProvider.COL_M_DEVICE_ID));
            user.setName(getValue(cursor, DbProvider.COL_M_NAME));
            user.setPhone(getValue(cursor, DbProvider.COL_M_PHONE));
            user.setMac(getValue(cursor, DbProvider.COL_M_MAC));
            user.setStatus(getValue(cursor, DbProvider.COL_M_STATUS));
            user.setModel(getValue(cursor, DbProvider.COL_M_MODEL));
            user.setParentId(getValue(cursor, DbProvider.COL_M_CREATED_BY));
            user.setAge(getValue(cursor, DbProvider.COL_M_AGE));
            user.setGender(getValue(cursor, DbProvider.COL_M_GENDER));
            user.setToken(getValue(cursor, DbProvider.COL_M_TOKEN));
            user.setPhotoUrl(getValue(cursor, DbProvider.COL_M_PHOTO_URL));

        } catch (SQLiteException e) {
            Log.e(TAG, "Exception : " + e.getMessage());
        }

        return user;
    }

    // TODO:Write a common method to get individual data of member
    // TODO: To use userID to get the name . Use message passing from caller class.
    public static String getMemberName(Context context, String userType) {

        String memberName = "";
        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {userType};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_NAME}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                memberName = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_NAME));
            }
            cursor.close();
        }

        return memberName;
    }

    public static ArrayList<String> getAllWearableIds(Context context) {
        ArrayList<String> userIdList = new ArrayList<String>();

        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_WATCH};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_ID}, whereClause, whereArgs, null);

        if (cursor != null) {

            int count = cursor.getCount();

            if (count > 0) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    userIdList.add(cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_ID)));
                }
            }
            cursor.close();
        }
        return userIdList;
    }

    public static ArrayList<String> getAllTrackerIds(Context context) {
        ArrayList<String> trackerIdList = new ArrayList<String>();
        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_TRACKER};
        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_ID}, whereClause, whereArgs, null);
        if (cursor != null) {
            int count = cursor.getCount();
            if (count > 0) {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()) {
                    trackerIdList.add(cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_ID)));
                }
            }
            cursor.close();
        }
        return trackerIdList;
    }

    public static String getMemberNameById(Context context, String userId) {

        String memberName = "";
        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {userId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_NAME}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                memberName = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_NAME));
            }
            cursor.close();
        }

        return memberName;
    }

    // Get image url by user id
    public static String getPhotoUrl(Context context, String userId) {

        String memberImgUrl = "";
        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {userId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_PHOTO_URL}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                memberImgUrl = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_PHOTO_URL));
            }
            cursor.close();
        }

        return memberImgUrl;
    }

    *//**
     * Returns user object
     *
     * @param context
     * @param userId  - id of which user has to be fetched
     * @return User object
     *//*
    public static User getUser(Context context, String userId) {
        User user = null;
        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {userId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI, null,
                whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                user = getUserObject(cursor);
            }
            cursor.close();
        }
        return user;
    }

    public static String getModel(Context context, String wearableId) {

        String model = "";
        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {wearableId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_MODEL}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                model = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_MODEL));
            }
            cursor.close();
        }

        return model;

    }

    public static String[] getAllMembersOfGroup(Context context, String groupId) {

        String members[] = null;
        String whereClause = DbProvider.COL_GROUP_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {groupId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.GROUP_MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_ID}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                members = new String[cursor.getCount()];

                for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
                    members[i] = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_ID));
                }

            }
            cursor.close();
        }

        return members;
    }

    public static ArrayList<User> getAllGroups(Context context) {

        ArrayList<User> groupList = new ArrayList();

        String whereClause = DbProvider.COL_GROUP_ADMIN_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {getMemberId(context)};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.GROUPS_CONTENT_URI, null,
                whereClause, whereArgs, null);
        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    User groupDetails = new User();

                    groupDetails.setId(getValue(cursor, DbProvider.COL_GROUP_ID));
                    groupDetails.setName(getValue(cursor, DbProvider.COL_GROUP_NAME));
                    groupDetails.setPhotoUrl(getValue(cursor, DbProvider.COL_GROUP_PHOTO_URL));
                    groupDetails.setStatus(String.valueOf(true));

                    groupList.add(groupDetails);

                    cursor.moveToNext();
                }
            }

            cursor.close();
        }

        return groupList;
    }

    public static ArrayList<User> getAllGroupsMembers(Context context, String groupId) {

        ArrayList<User> memberList = new ArrayList();

        String whereClause = DbProvider.COL_GROUP_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {groupId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.GROUP_MEMBERS_CONTENT_URI, null,
                whereClause, whereArgs, null);

        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                while (cursor.isAfterLast() == false) {

                    String memberId = getValue(cursor, DbProvider.COL_M_ID);
                    User memberDetails = getUser(context, memberId);

                    if (memberDetails != null) {
                        memberList.add(memberDetails);
                    }

                    cursor.moveToNext();
                }
            }

            cursor.close();
        }

        return memberList;
    }

    public static Uri addGroupMember(Context context, String groupId, String memberId) {

        ContentValues groupMemberInfo = new ContentValues();
        groupMemberInfo.put(DbProvider.COL_M_ID, memberId);
        groupMemberInfo.put(DbProvider.COL_GROUP_ID, groupId);

        return DbProvider.getInstance(context).insert(
                DbProvider.GROUP_MEMBERS_CONTENT_URI, groupMemberInfo);

    }

    // Methods used for Safezone
    // Converts SafeZoneData to ContentValues object
    public static ContentValues getSafeZoneContentValues(SafeZoneData data) {

        ContentValues values = new ContentValues();

        values.put(DbProvider.COL_M_DEVICE_ID, data.getDeviceId());
        values.put(DbProvider.COL_SZ_ID, data.getSafezoneId());
        values.put(DbProvider.COL_SZ_TYPE, data.getType());
        values.put(DbProvider.COL_ADDRESS, data.getAddress());
        values.put(DbProvider.COL_NAME, data.getName());
        values.put(DbProvider.COL_LATITUDE, data.getLatitude());
        values.put(DbProvider.COL_LONGITUDE, data.getLongitude());
        values.put(DbProvider.COL_RADIUS, data.getRadius());

        return values;
    }

    // Add safezone
    public static void addSafeZone(Context context, ContentValues values) {
        DbProvider.getInstance(context).insert(DbProvider.SAFE_ZONE_CONTENT_URI, values);
    }

    // Remove safezone
    public static int removeSafeZone(Context context, String safezoneId) {

        return DbProvider.getInstance(context).delete(
                DbProvider.SAFE_ZONE_CONTENT_URI,
                DbProvider.COL_SZ_ID + "=?",
                new String[]{safezoneId});
    }

    // Update safezone
    public static void updateSafezone(Context context, SafeZoneData data) {

        DbProvider.getInstance(context).update(
                DbProvider.SAFE_ZONE_CONTENT_URI,
                getSafeZoneContentValues(data),
                DbProvider.COL_SZ_ID + EQUAL_QUESTION_MARK,
                new String[]{data.getSafezoneId()});
    }

    // Get all safezones
    public static ArrayList<SafeZoneData> getAllSafeZones(Context context, String deviceId) {

        ArrayList<SafeZoneData> safeZones = new ArrayList<SafeZoneData>();

        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.SAFE_ZONE_CONTENT_URI,
                null, whereClause, whereArgs, null);

        if (cursor != null) {

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    SafeZoneData data = new SafeZoneData();

                    data.setDeviceId(deviceId);
                    data.setSafezoneId(cursor.getString(cursor.getColumnIndex
                            (DbProvider.COL_SZ_ID)));
                    data.setType(cursor.getString(cursor.getColumnIndex
                            (DbProvider.COL_SZ_TYPE)));
                    data.setName(cursor.getString(cursor.getColumnIndex
                            (DbProvider.COL_NAME)));

                    String address = cursor.getString(cursor.getColumnIndex(DbProvider.COL_ADDRESS));
                    if (address.equals(Constants.NO_ADDRESS)) address = "";
                    data.setAddress(address);

                    data.setLatitude(cursor.getDouble(cursor.getColumnIndex
                            (DbProvider.COL_LATITUDE)));
                    data.setLongitude(cursor.getDouble(cursor.getColumnIndex
                            (DbProvider.COL_LONGITUDE)));
                    data.setRadius(cursor.getInt(cursor.getColumnIndex
                            (DbProvider.COL_RADIUS)));
                    safeZones.add(data);
                }
            }
            cursor.close();
        }

        return safeZones;
    }

    // Get safezone by id
    public static SafeZoneData getSafeZoneById(Context context, String safeZoneId) {

        SafeZoneData data = null;

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.SAFE_ZONE_CONTENT_URI,
                null, DbProvider.COL_SZ_ID + " =?", new String[]{safeZoneId}, null);

        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                data = new SafeZoneData();

                data.setSafezoneId(cursor.getString(cursor.getColumnIndex
                        (DbProvider.COL_SZ_ID)));
                data.setType(cursor.getString(cursor.getColumnIndex
                        (DbProvider.COL_SZ_TYPE)));
                data.setName(cursor.getString(cursor.getColumnIndex
                        (DbProvider.COL_NAME)));

                String address = cursor.getString(cursor.getColumnIndex(DbProvider.COL_ADDRESS));
                if (address.equals(Constants.NO_ADDRESS)) address = "";
                data.setAddress(address);

                data.setAddress(cursor.getString(cursor.getColumnIndex
                        (DbProvider.COL_ADDRESS)));
                data.setLatitude(cursor.getDouble(cursor.getColumnIndex
                        (DbProvider.COL_LATITUDE)));
                data.setLongitude(cursor.getDouble(cursor.getColumnIndex
                        (DbProvider.COL_LONGITUDE)));
                data.setRadius(cursor.getInt(cursor.getColumnIndex
                        (DbProvider.COL_RADIUS)));
            }
            cursor.close();
        }

        return data;
    }

    // Parse SafeZoneData from JSONObject
    public static SafeZoneData parseSafezoneObject(String deviceId, JSONObject safezone) {

        SafeZoneData data = null;

        try {
            data = new SafeZoneData(
                    deviceId,
                    safezone.getString(GeofenceConstants.SZ_ID),
                    safezone.getString(GeofenceConstants.SZ_TYPE),
                    safezone.optString(GeofenceConstants.SZ_ADDRESS, ""),
                    safezone.getString(GeofenceConstants.SZ_TITLE),
                    safezone.getDouble(GeofenceConstants.SZ_LATITUDE),
                    safezone.getDouble(GeofenceConstants.SZ_LONGITUDE),
                    safezone.getInt(GeofenceConstants.SZ_RADIUS));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Delete all safe zones from local DB for the given device id.
    public static void deleteSafezones(Context context, String deviceId) {
        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};
        DbProvider.getInstance(context).delete(DbProvider.SAFE_ZONE_CONTENT_URI, whereClause, whereArgs);
    }

    public static int getDeviceType(Context context, String wearableId) {

        int isTracker = 0;
        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {wearableId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_DEVICE_TYPE}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                isTracker = cursor.getInt(cursor.getColumnIndex(DbProvider.COL_M_DEVICE_TYPE));
            }
            cursor.close();
        }

        return isTracker;

    }

    public static boolean updateMemberToken(Context context, String newToken) {

        String whereClause = DbProvider.COL_M_TYPE + EQUAL_QUESTION_MARK;
        String[] whereArgs = {Constants.USERTYPE_CURRENT_USER};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbProvider.COL_M_TOKEN, newToken);

        int rows = DbProvider.getInstance(context).update(DbProvider.MEMBERS_CONTENT_URI,
                contentValues, whereClause, whereArgs);
        return rows > 0 ? true : false;

    }

    public static String getGroupName(Context context, String groupId) {

        String groupName = null;
        String whereClause = DbProvider.COL_GROUP_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {groupId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.GROUPS_CONTENT_URI,
                new String[]{DbProvider.COL_GROUP_NAME}, whereClause, whereArgs, null);

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                groupName = cursor.getString(cursor.getColumnIndex(DbProvider.COL_GROUP_NAME));
            }
            cursor.close();
        }

        return groupName;
    }

    public static String getWearableIdForDeviceId(Context context, String deviceId) {

        String wearableId = null;

        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_ID},
                whereClause, whereArgs, null);

        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                wearableId = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_ID));
            }
            cursor.close();
        }

        return wearableId;
    }

    public static String getDeviceIdForMemberId(Context context, String memberId) {

        String deviceId = null;

        String whereClause = DbProvider.COL_M_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {memberId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.MEMBERS_CONTENT_URI,
                new String[]{DbProvider.COL_M_DEVICE_ID},
                whereClause, whereArgs, null);

        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                deviceId = cursor.getString(cursor.getColumnIndex(DbProvider.COL_M_DEVICE_ID));
            }
            cursor.close();
        }

        return deviceId;
    }

    public static void setSWInfo(Context context, DeviceSettingDetails details) {

        if (details != null) {
            String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
            String[] whereArgs = {details.getDeviceId()};

            ContentValues swVersionInfo = new ContentValues();
            swVersionInfo.put(DbProvider.COL_SW_VERSION, details.getDeviceSWCurrentVersion());
            swVersionInfo.put(DbProvider.COL_SW_SYNC_STATUS, details.getDeviceSWSyncStatus());
            swVersionInfo.put(DbProvider.COL_SW_SYNC_TIME, System.currentTimeMillis());

            int rows = DbProvider.getInstance(context).update(DbProvider.CONFIGURATION_CONTENT_URI,
                    swVersionInfo, whereClause, whereArgs);

            if (rows <= 0) {
                swVersionInfo.put(DbProvider.COL_M_DEVICE_ID, details.getDeviceId());
                DbProvider.getInstance(context).insert(DbProvider.CONFIGURATION_CONTENT_URI, swVersionInfo);
            }
        }
    }

    public static DeviceSettingDetails getSWInfo(Context context, String deviceId) {

        DeviceSettingDetails versionInfo = null;
        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.CONFIGURATION_CONTENT_URI,
                new String[]{DbProvider.COL_SW_VERSION, DbProvider.COL_SW_SYNC_TIME},
                whereClause, whereArgs, null);

        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                versionInfo = new DeviceSettingDetails(deviceId);
                versionInfo.setDeviceSWCurrentVersion(
                        cursor.getString(cursor.getColumnIndex(DbProvider.COL_SW_VERSION)));
                versionInfo.setDeviceSWSyncTime(
                        cursor.getLong(cursor.getColumnIndex(DbProvider.COL_SW_SYNC_TIME)));
            }
            cursor.close();
        }

        return versionInfo;
    }

    public static void setConfiguration(Context context, DeviceSettingDetails info) {

        if(info == null){
            return;
        }

        int desiredInterval = info.getConfigDesInterval();
        int reportedInterval = info.getConfigRepInterval();
        String reportedVersion = info.getConfigRepVersion();
        String desiredVersion = info.getConfigDesVersion();
        String repDays = info.getConfigRepDays();
        String desDays = info.getConfigDesDays();
        String repStartTime = info.getConfigRepStartTime();
        String desStartTime = info.getConfigDesStartTime();
        String repEndTime = info.getConfigRepEndTime();
        String desEndTime = info.getConfigDesEndTime();

        ContentValues configValues = new ContentValues();
        configValues.put(DbProvider.COL_CONFIG_REP_INTERVAL, reportedInterval);
        configValues.put(DbProvider.COL_CONFIG_REP_VERSION, reportedVersion);
        configValues.put(DbProvider.COL_CONFIG_REP_DAYS, repDays);
        configValues.put(DbProvider.COL_CONFIG_REP_START_TIME, repStartTime);
        configValues.put(DbProvider.COL_CONFIG_REP_END_TIME, repEndTime);
        configValues.put(DbProvider.COL_CONFIG_DES_INTERVAL, desiredInterval);
        configValues.put(DbProvider.COL_CONFIG_DES_VERSION, desiredVersion);
        configValues.put(DbProvider.COL_CONFIG_DES_DAYS, desDays);
        configValues.put(DbProvider.COL_CONFIG_DES_START_TIME, desStartTime);
        configValues.put(DbProvider.COL_CONFIG_DES_END_TIME, desEndTime);

        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {info.getDeviceId()};

        int rows = DbProvider.getInstance(context).update(DbProvider.CONFIGURATION_CONTENT_URI,
                configValues, whereClause, whereArgs);

        if (rows <= 0) {
            configValues.put(DbProvider.COL_M_DEVICE_ID, info.getDeviceId());
            DbProvider.getInstance(context).insert(DbProvider.CONFIGURATION_CONTENT_URI, configValues);
        }
    }

    public static void updateDesiredVersion(Context context, String deviceId, String version) {

        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbProvider.COL_CONFIG_DES_VERSION, version);

        DbProvider.getInstance(context).update(DbProvider.CONFIGURATION_CONTENT_URI,
                contentValues, whereClause, whereArgs);
    }

    public static DeviceSettingDetails getConfiguration(Context context, String deviceId) {

        DeviceSettingDetails config = null;

        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;

        String[] whereArgs = {deviceId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.CONFIGURATION_CONTENT_URI, null,
                whereClause, whereArgs, null);

        if (cursor != null) {

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {

                config = new DeviceSettingDetails(deviceId);

                config.setConfigDesInterval(cursor.getInt(cursor.getColumnIndex(DbProvider.COL_CONFIG_DES_INTERVAL)));
                config.setConfigRepInterval(cursor.getInt(cursor.getColumnIndex(DbProvider.COL_CONFIG_REP_INTERVAL)));
                config.setConfigDesVersion(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_DES_VERSION)));
                config.setConfigRepVersion(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_REP_VERSION)));
                config.setConfigDesDays(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_DES_DAYS)));
                config.setConfigRepDays(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_REP_DAYS)));
                config.setConfigDesStartTime(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_DES_START_TIME)));
                config.setConfigRepStartTime(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_REP_START_TIME)));
                config.setConfigDesEndTime(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_DES_END_TIME)));
                config.setConfigRepEndTime(cursor.getString(cursor.getColumnIndex(DbProvider.COL_CONFIG_REP_END_TIME)));
            }
            cursor.close();
        }

        return config;
    }

    public static void removeSWAndConfig(Context context, String deviceId) {
        String whereClause = DbProvider.COL_M_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};
        DbProvider.getInstance(context).delete(DbProvider.CONFIGURATION_CONTENT_URI,
                whereClause, whereArgs);
    }

    public static void addContact(Context context, String deviceId, JSONObject json) {

        try {

            ContentValues values = new ContentValues();
            values.put(DbProvider.COL_ID, json.getString(ParserConstants.JSONOBJ_PB_ID));
            values.put(DbProvider.COL_CONTACT_DEVICE_ID, deviceId);
            values.put(DbProvider.COL_CONTACT_NAME, json.getString(ParserConstants.JSONOBJ_PB_NAME));
            values.put(DbProvider.COL_CONTACT_NUMBER, json.getString(ParserConstants.JSONOBJ_PB_NUMBER));
            values.put(DbProvider.COL_CONTACT_PRIORITY, json.getInt(ParserConstants.JSONOBJ_PB_PRIORITY));
            values.put(DbProvider.COL_CONTACT_TYPE, json.getString(ParserConstants.JSONOBJ_PB_TYPE));

            Uri uri = DbProvider.getInstance(context).insert(DbProvider.KIDS_CONTACTS_CONTENT_URI, values);

            Log.d(TAG, "New contact added : " + uri.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteContact(Context context, String contactId) {
        String whereClause = DbProvider.COL_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {contactId};
        DbProvider.getInstance(context).delete(DbProvider.KIDS_CONTACTS_CONTENT_URI, whereClause, whereArgs);
    }

    public static void deleteContacts(Context context, String deviceId) {
        String whereClause = DbProvider.COL_CONTACT_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};
        DbProvider.getInstance(context).delete(DbProvider.KIDS_CONTACTS_CONTENT_URI, whereClause, whereArgs);
    }

    public static List<ContactDetails> getContacts(Context context, String deviceId) {

        List<ContactDetails> contactList = new ArrayList<ContactDetails>();

        String where = DbProvider.COL_CONTACT_DEVICE_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {deviceId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.KIDS_CONTACTS_CONTENT_URI,
                null, where, whereArgs, null);

        if (cursor != null && cursor.moveToFirst()) {

            do {

                ContactDetails contact = new ContactDetails();

                contact.setName(getValue(cursor, DbProvider.COL_CONTACT_NAME));
                contact.setNumber(getValue(cursor, DbProvider.COL_CONTACT_NUMBER));
                contact.setType(getValue(cursor, DbProvider.COL_CONTACT_TYPE));
                contact.setContactId(getValue(cursor, DbProvider.COL_ID));
                contact.setDeviceId(getValue(cursor, DbProvider.COL_CONTACT_DEVICE_ID));
                contact.setPriority(cursor.getInt(cursor.getColumnIndex(
                        DbProvider.COL_CONTACT_PRIORITY)));

                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return contactList;
    }

    public static boolean contactExist(Context context, String deviceId, String phoneNumber) {

        List<ContactDetails> contactList = getContacts(context, deviceId);

        boolean contactExist = false;

        for (ContactDetails contact : contactList) {
            if (contact.getNumber().equals(phoneNumber)) {
                contactExist = true;
            }
        }
        return contactExist;
    }
    public static final Uri getUriForResource( Context context, int resId)
            throws Resources.NotFoundException {
        Resources res = context.getResources();

        Uri imageUri = (new Uri.Builder())
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(res.getResourcePackageName(resId))
                .appendPath(res.getResourceTypeName(resId))
                .appendPath(res.getResourceEntryName(resId))
                .build();
        return imageUri;
    }

    public static List<String> getAudioMsgPathList(Context context, String wearableId) {
        List<String> audioMsgPathList = new ArrayList<>();
        String[] projection = {FILE_PATH};
        String whereClause = DbProvider.FROM_USER_ID + EQUAL_QUESTION_MARK +
                SYMBOL_OR + DbProvider.TO_USER_ID + EQUAL_QUESTION_MARK;
        String[] whereArgs = {wearableId, wearableId};

        Cursor cursor = DbProvider.getInstance(context).query(DbProvider.SMART_MESSAGING_CONTENT_URI,
                projection, whereClause, whereArgs, null);

        if (cursor != null && cursor.moveToFirst()) {

            try {
                do {
                    String filePath = getValue(cursor, FILE_PATH);
                    if (!TextUtils.isEmpty(filePath)) {
                        audioMsgPathList.add(filePath);
                    }
                } while (cursor.moveToNext());

            } catch (SQLiteException e) {
                Log.e(TAG, "Exception : " + e.getMessage());
            } finally {
                cursor.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return audioMsgPathList;
    }*/
}