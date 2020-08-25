package com.example.bachelorthesisclient.util;

import android.content.Context;

public class AppContext {
    private static Context appContext;

    public static void saveAppContext(Context context) {
        if (appContext == null) {
            appContext = context;
        }
    }

    public static Context getAppContext() {
        return appContext;
    }
}
