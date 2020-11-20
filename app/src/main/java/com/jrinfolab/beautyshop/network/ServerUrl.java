package com.jrinfolab.beautyshop.network;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class ServerUrl {

    public static final String BASE_URL  = "https://staging-spa-core.df.r.appspot.com/api/user";

    public static final String API      = "api";
    public static final String USER     = "user";
    public static final String USERS     = "users";
    public static final String GROUP    = "group";
    public static final String EVENT    = "event";
    public static final String EVENTS   = "events";
    public static final String SEARCH   = "search";
    public static final String SAFEZONE = "safezones";
    public static final String FILE     = "file";
    public static final String OBJECTS  = "objects";
    public static final String PROFILES = "profiles";
    public static final String DEVICES  = "devices";
    public static final String SETTINGS = "settings";
    public static final String STATUS   = "status";
    public static final String SWUPDATE = "swupdate";
    public static final String PHONE_BOOK  = "phonebooks";
    public static final String TOKENS = "tokens";
    public static final String VERIFY = "verify";
    public static final String DEVICE_STATUS = "devicestatuses";
    public static final String MESSAGES = "messages";
    public static final String JOBS = "jobs";

    public static final String REFRESH_TOKEN  = "refreshtoken";
    public static final String ADD_DEVICE     = "addwearable";
    public static final String UPLOAD_FILE    = "uploadfile";
    public static final String GEOFENCE_LOGIN = "applogin";
    public static final String UGS_TOKEN      = "ugs_token";
    public static final String GEOFENCE_TOKEN = "app_token";

    public static final String USER_LOGIN           = "login";
    public static final String USER_LOGOUT          = "logout";
    public static final String USER_REGISTER        = "register";
    public static final String USER_LOGIN_GOOGLE    = "extapilogin";
    public static final String USER_REGISTER_GOOGLE = "registerextapiuser";
    public static final String USER_REGISTER_TOKEN  = "regtoken";
    public static final String USER_FORGOT_PASSWORD = "forgotpassword";
    public static final String USER_RESET_PASSWORD  = "resetpassword";
    public static final String USER_ACTIVATION      = "activation";

    // GOOGLE LOGIN
    public static final String MAP_SUGGESTION_URL  = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
    public static final String MAP_DIRECTIONS_URL  = "http://maps.google.com/maps?daddr=%f,%f";
    public static final String GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps";

    // GOOGLE MAP
    public static final String GOOGLE_ACCOUNT_TYPE = "com.google";
    private static final String PROFILE_SCOPE      = "https://www.googleapis.com/auth/userinfo.profile";
    private static final String EMAIL_SCOPE        = "https://www.googleapis.com/auth/userinfo.email";
    public static final String GOOGLE_SCOPES       = "oauth2:" + PROFILE_SCOPE + " " + EMAIL_SCOPE;

    //FCM
    private static final String SNS = "sns";
    private static final String ENDPOINT = "endpoint";


    public static String getEventUrl(Context context, String type, String userId, HashMap<String, String> query) {

        String[] pathArray = { API, type, userId, EVENT };

        String eventUrl = BASE_URL + buildPath(pathArray);

        Uri.Builder builder = new Uri.Builder();
        builder.encodedPath(eventUrl);

        for (Map.Entry<String, String> entry : query.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build().toString();
    }

    private static String buildPath(String[] pathArray) {

        StringBuilder path = new StringBuilder();

        for (String pathString : pathArray) {
            path = path.append("/" + pathString);
        }

        return path.toString();
    }
}