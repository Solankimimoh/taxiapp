package com.example.mickeymouse.caxi.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by MickeyMouse on 23-Mar-17.
 */

public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();


    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    Context context;


    int PRIVATE_MODE = 0;


    // Shared preferences file name
    private static final String PREF_NAME = "CaxiLogin";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String USER_NAME_SESSION = "username";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setUser(String userid) {
        editor.putString(USER_NAME_SESSION, userid);
        editor.commit();
    }

    public String userNameSession()
    {
        return  sharedPreferences.getString(USER_NAME_SESSION,null);
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.e(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

}
