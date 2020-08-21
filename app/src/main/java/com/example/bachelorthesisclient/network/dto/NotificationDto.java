package com.example.bachelorthesisclient.network.dto;

import java.util.Map;

public class NotificationDto {
    private Map<String, String> data;

    public NotificationDto() {
    }

    public NotificationDto(Map<String, String > data) {
        this.data = data;
    }

    public Map<String, String > getData() {
        return data;
    }

    public void setData(Map<String, String > data) {
        this.data = data;
    }
}
