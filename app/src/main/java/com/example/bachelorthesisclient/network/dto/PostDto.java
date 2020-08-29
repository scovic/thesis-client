package com.example.bachelorthesisclient.network.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDto {
    int id;
    private String text;
    private int authorId;
    private double latitude;
    private double longitude;
    private long createdAt;
    private long updatedAt;
    private List<String> attachmentNames;

    public PostDto(int id, String text, int authorId, double lat, double lon, List<String> attachmentNames) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.latitude = lat;
        this.longitude = lon;
        this.attachmentNames = attachmentNames;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getAttachmentNames() {
        return attachmentNames;
    }

    public void setAttachmentNames(List<String> attachmentNames) {
        this.attachmentNames = attachmentNames;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
