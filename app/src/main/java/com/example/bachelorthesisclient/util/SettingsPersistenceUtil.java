package com.example.bachelorthesisclient.util;

import com.example.bachelorthesisclient.model.Settings;
import com.example.bachelorthesisclient.wrapper.GsonWrapper;
import com.example.bachelorthesisclient.wrapper.SharedPreferenceWrapper;

public class SettingsPersistenceUtil {
    private static final String SETTINGS_KEY = "SETTINGS_KEY";

    public static void persistSettings(Settings settings) {
        String settingsJson = GsonWrapper.toJson(settings);
        SharedPreferenceWrapper.getInstance().put(SETTINGS_KEY, settingsJson);
    }

    public static Settings getSettings() {
        String settingsJson = getSettingsJson();

        if (settingsJson.isEmpty()) {
            Settings settings = new Settings(150, true);
            persistSettings(settings);
            return settings;
        }

        return (Settings) GsonWrapper.fromJson(settingsJson, Settings.class);
    }

    public static boolean canReceiveInfoNotifications() {
        Settings settings = getSettings();
        return settings.isReceiveInfoNotifications();
    }

    private static String getSettingsJson() {
        return (String) SharedPreferenceWrapper.getInstance().get(SETTINGS_KEY, "");
    }
}
