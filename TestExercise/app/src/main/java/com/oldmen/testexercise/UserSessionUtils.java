package com.oldmen.testexercise;


import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionUtils {

    public static String SKIP_REGISTRATION_KEY = "Skip registration";
    public static String LOGIN_KEY = "login";
    public static String TOKEN_KEY = "token";
    public static String USER_KEY = "User";

    public static String NO_KEY = "No";
    public static String YES_KEY = "Yes";


    public static UserContainer readSession(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        String nothing = "nothing";

        UserContainer user = new UserContainer();
        user.setUserLogin(sharedPreferences.getString(LOGIN_KEY, nothing));
        user.setUserSkipRegistrMode(sharedPreferences.getString(SKIP_REGISTRATION_KEY, nothing));
        user.setUserToken(sharedPreferences.getString(TOKEN_KEY, nothing));
        return user;
    }

    public static void saveSession(Context context, UserContainer user) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, user.getUserLogin())
                .putString(SKIP_REGISTRATION_KEY, user.getUserSkipRegistrMode())
                .putString(TOKEN_KEY, user.getUserToken())
                .apply();
    }

    public static void deleteSession(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .clear()
                .apply();
    }

    public static void saveSkipRegistrationMode(Context context, String toSkip) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SKIP_REGISTRATION_KEY, toSkip)
                .apply();
    }
}
