package com.example.bachelorthesisclient.repository;

public class RepositoryFactory {
    public static final String FEED_REPOSITORY = "FeedRepository";
    public static final String NOTIFICATION_REPOSITORY = "NotificationRepository";
    public static final String SETTINGS_REPOSITORY = "SettingsRepository";

    public static Object get(String name) {
        switch (name) {
            case FEED_REPOSITORY:
                return FeedRepositoryImpl.getInstance();
            case NOTIFICATION_REPOSITORY:
                return NotificationRepositoryImpl.getInstance();
            case SETTINGS_REPOSITORY:
                return SettingsRepositoryImpl.getInstance();
            default:
                return null;
        }
    }
}
