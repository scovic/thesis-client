package com.example.bachelorthesisclient.wrapper;

import com.example.bachelorthesisclient.util.AppContext;
import com.example.bachelorthesisclient.util.SettingsPersistenceUtil;
import com.pusher.pushnotifications.PushNotifications;

public class PushNotificationsWrapper {
    private final static String INFO_KEY = "info";
    private final static String WARNING_KEY = "warning";

    public static void init() {
        PushNotifications.start(AppContext.getAppContext(), "aa993276-080e-4b51-b54d-94400984ffa2");

        if (SettingsPersistenceUtil.getSettings().isReceiveInfoNotifications()) {
            addInfoInterest();
        }

        addWarningInterest();
    }

    public static void addInfoInterest() {
        PushNotifications.addDeviceInterest(INFO_KEY);
    }

    public static void removeInfoInterest() {
        PushNotifications.removeDeviceInterest(INFO_KEY);
    }

    private static void addWarningInterest() {
        PushNotifications.addDeviceInterest(WARNING_KEY);
    }
}
