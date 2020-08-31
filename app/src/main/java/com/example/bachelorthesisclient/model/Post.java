package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.network.dto.PostDto;

import org.osmdroid.util.GeoPoint;

import java.util.Date;
import java.util.List;

public class Post {
    private int id;
    private String text;
    private int authorId;
    private Date createdAt;
    private Date updatedAt;
    private List<String> attachmentNames;
    private GeoPoint location;

    public Post(PostDto postDto) {
        this.id = postDto.getId();
        this.text = postDto.getText();
        this.authorId = postDto.getAuthorId();
        this.createdAt = new Date(postDto.getCreatedAt());
        this.updatedAt = new Date(postDto.getUpdatedAt());
        this.attachmentNames = postDto.getAttachmentNames();
        this.location = new GeoPoint(postDto.getLatitude(), postDto.getLongitude());
    }

    public Post(String text, int authorId, GeoPoint location) {
        this.text = text;
        this.authorId = authorId;
        this.location = location;
    }

    public PostDto getData() {
        return new PostDto(
                getId(),
                getText(),
                getAuthorId(),
                getLocation().getLatitude(),
                getLocation().getLongitude(),
                getAttachmentNames()
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAuthorId() {
        return authorId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<String> getAttachmentNames() {
        return attachmentNames;
    }

    public boolean hasLocation() {
        return location.getLatitude() != 0 || location.getLongitude() != 0;
    }

    public GeoPoint getLocation() {
        return location;
    }

}
