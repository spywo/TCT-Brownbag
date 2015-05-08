package com.autodesk.icp.community.mobile.util;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.autodesk.icp.community.common.model.User;
import com.autodesk.icp.community.common.util.JSONReadWriteHelper;
import com.autodesk.icp.community.mobile.activity.Login;

public class SessionManager {

    private SharedPreferences pref;

    private Editor editor;

    private Context context;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "USER_CONTEXT";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USER = "user";

    public static final String KEY_JSESSIONID = "jsessionid";

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(User user) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER, JSONReadWriteHelper.serializeToJSON(user));

        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status If false it will redirect user to login page Else won't do
     * anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);
        }
    }

    public User getUserDetails() {
        User user = null;
        String userJson = pref.getString(KEY_USER, null);
        if (StringUtils.isNotBlank(userJson)) {
            InputStream is = IOUtils.toInputStream(userJson);
            user = JSONReadWriteHelper.deSerializeJSON(is, User.class);
        }

        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setJSESSIONID(String jsessionid) {
        editor.putString(KEY_JSESSIONID, jsessionid);
        editor.commit();
    }

    public String getJSESSIONID() {
        return pref.getString(KEY_JSESSIONID, null);
    }
}