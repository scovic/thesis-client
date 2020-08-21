package com.example.bachelorthesisclient.model;

import java.util.HashMap;
import java.util.Map;

public class NotificationData {
    private final String TITLE_KEY = "title";
    private final String BODY_KEY = "body";
    private final String SENDER_ID_KEY = "senderId";
    private final String POST_ID_KEY = "postId";
    private final String TAG_KEY = "tag";
    private final String LATITUDE_KEY = "latitude";
    private final String LONGITUDE_KEY = "longitude";
    private final String INFO_TAG_VALUE = "info";
    private final String WARNING_TAG_VALUE = "warning";

    private Map<String, String> data;

    public NotificationData() {
        data = new HashMap<>();
    }

    public NotificationData(Map<String, String> data) {
        this.data = data;
    }

    public NotificationData setTitle(String title) {
        data.put(this.TITLE_KEY, title);
        return this;
    }

    public String getTitle() {
        return (String) data.get(this.TITLE_KEY);
    }

    public NotificationData setBody(String body) {
        data.put(this.BODY_KEY, body);
        return this;
    }

    public String getBody() {
        return (String) data.get(this.BODY_KEY);
    }

    public NotificationData setSender(int senderId) {
        data.put(this.SENDER_ID_KEY, String.valueOf(senderId));
        return this;
    }

    public int getSender() {
        return Integer.valueOf(data.get(this.SENDER_ID_KEY));
    }

    public NotificationData setPostId(int postId) {
        data.put(this.POST_ID_KEY, String.valueOf(postId));
        return this;
    }

    public int getPostId() {
        return Integer.valueOf( data.get(this.POST_ID_KEY));
    }

    public NotificationData setInfoTag() {
        data.put(this.TAG_KEY, this.INFO_TAG_VALUE);
        return this;
    }

    public NotificationData setWarningTag() {
        data.put(this.TAG_KEY, this.WARNING_TAG_VALUE);
        return this;
    }

    public boolean isInfoNotification() {
        return data.get(this.TAG_KEY).equals(this.INFO_TAG_VALUE);
    }

    public NotificationData setLocation(Location location) {
        data.put(this.LATITUDE_KEY, String.valueOf(location.getLatitude()));
        data.put(this.LONGITUDE_KEY, String.valueOf(location.getLongitude()));
        return this;
    }

    public Location getLocation() {
        double latitude = Double.valueOf(data.get(this.LATITUDE_KEY));
        double longitude = Double.valueOf(data.get(this.LONGITUDE_KEY));
        return new Location(latitude, longitude);
    }

    public Map<String, String> build() {
        return this.data;
    }
}
