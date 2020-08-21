package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.config.Values;

public class Settings {
    private float currentDistance;
    private boolean receiveInfoNotifications;

    public Settings(float currentDistance, boolean receiveInfoNotifications) {
        this.currentDistance = currentDistance;
        this.receiveInfoNotifications = receiveInfoNotifications;
    }

    public float getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(float currentDistance) {
        if (currentDistance > Values.MIN_DISTANCE && currentDistance < Values.MAX_DISTANCE) {
            this.currentDistance = currentDistance;
        }
    }

    public boolean isReceiveInfoNotifications() {
        return receiveInfoNotifications;
    }

    public void setReceiveInfoNotifications(boolean receiveInfoNotifications) {
        this.receiveInfoNotifications = receiveInfoNotifications;
    }
}
