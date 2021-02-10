package com.bd.esellers.userAuth;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bd.esellers.userProfile.userModel.Users;

import static com.bd.esellers.utility.Constant.BASE_URL;


public class SessionManager {

    private static final String TAG = "SessionManager";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final int MODE = Context.MODE_PRIVATE;
    public static final String PREF_NAME = "Session";
    public static final String LOGIN = "IS_LOGIN";
    public static final String TOKEN = "TOKEN";
    public static final String ID = "ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String MOBILE = "MOBILE";
    public static final String ADDRESS = "ADDRESS";
    public static final String PHOTO_URL = "PHOTO_URL";
    public static final String PHOTO = "PHOTO";
    public static final String STATUS = "USER_STATUS";
    public static final String HAS_ADDRESS = "HAS_ADDRESS";



    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
        editor = sharedPreferences.edit();
//        editor.putBoolean(LOGIN, false);
        editor.apply();
    }

    public void createSession(Users user) {
        Log.d(TAG, "createSession: "+user.isHasDifferentAddress());
        Log.d(TAG, "\n : "+user.getAddress());
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, String.valueOf(user.getId()));
        editor.putString(USER_NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(MOBILE, user.getMobile());
        editor.putString(ADDRESS, user.getAddress());
        editor.putString(PHOTO_URL, BASE_URL+user.getUserProfilePhoto());
        editor.putString(PHOTO, user.getProfileStringImg());
        editor.putString(TOKEN, user.getToken());
        editor.putBoolean(STATUS, user.isProfileComplete());
        if (user.isHasDifferentAddress().contains("1"))
            editor.putBoolean(HAS_ADDRESS, true);
        else
            editor.putBoolean(HAS_ADDRESS, false);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

//    public boolean checkLogin() {
//
//        if (!this.isLogin()) {
//            Intent i = new Intent(context, LoginActivity.class);
//
//            // Closing all the Activities from stack
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//            ((Activity) context).finish();
//            return true;
//        }
//        return false;
//    }

    public void clearSession() {
        editor.clear();
        editor.putBoolean(LOGIN, false);
        editor.commit();

    }


    /*user status
    * if 1 -> user profile is complete
    * else 0 -> incomplete user profile
    * */
}
