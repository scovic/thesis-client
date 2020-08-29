package com.example.bachelorthesisclient.util;

import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;

import com.example.bachelorthesisclient.model.LoggedUserPersistence;
import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.wrapper.GsonWrapper;
import com.example.bachelorthesisclient.wrapper.SharedPreferenceWrapper;

public class LoggedInUserPersistenceUtil {
    private static final String LOGGED_USER_KEY = "LOGGED_USER_KEY";

    public static Boolean isLoggedIn() {
        return !SharedPreferenceWrapper.getInstance().get(LOGGED_USER_KEY, "").equals("");
    }

    public static void setLoggedInUserData(String key) {
        SharedPreferenceWrapper.getInstance().put(LOGGED_USER_KEY, key);
    }

    public static String getToken() {
        String json = getLoggedUserJson();
        LoggedUserPersistence loggedUserPersistence = (LoggedUserPersistence) GsonWrapper.fromJson(json, LoggedUserPersistence.class);

        return loggedUserPersistence.getToken();
    }

    public static int getUserId() {
        JWT jwt = getJwtToken();
        return jwt.getClaim("id").asInt();
    }

    public static User getUser() {
        JWT jwt = getJwtToken();
        return new User(
                jwt.getClaim("id").asInt(),
                jwt.getClaim("email").asString(),
                jwt.getClaim("firstName").asString(),
                jwt.getClaim("lastName").asString()
        );
    }

    public static void signOut() {
        SharedPreferenceWrapper.getInstance().remove(LOGGED_USER_KEY);
    }

    private static JWT getJwtToken() {
        String json = getLoggedUserJson();
        LoggedUserPersistence loggedInUser = (LoggedUserPersistence) GsonWrapper.fromJson(json, LoggedUserPersistence.class);
        return new JWT(loggedInUser.getToken());
    }

    private static String getLoggedUserJson() {
        return (String) SharedPreferenceWrapper.getInstance().get(LOGGED_USER_KEY, "");
    }
}
