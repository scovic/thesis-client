package com.example.bachelorthesisclient.network.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDto {
    int id;
    private String text;
    private int authorId;
    private Date createdAt;
    private Date updatedAt;
    private List<String> attachmentNames;

    public PostDto() {
        attachmentNames = new ArrayList<>();
    }

    public PostDto(int id, String text, int authorId) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
    }

    public PostDto(String text, int authorId) {
        this.id = -1;
        this.text = text;
        this.authorId = authorId;
    }

    public PostDto(int id, String text, int authorId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getAttachmentNames() {
        return attachmentNames;
    }

    public void setAttachmentNames(List<String> attachmentNames) {
        this.attachmentNames = attachmentNames;
    }
}
