package com.example.bachelorthesisclient.util;

import android.content.Context;

public class AppContext {
    private static Context appContext;

    public static void saveAppContext(Context context) {
        appContext = context;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
