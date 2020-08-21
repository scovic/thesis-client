package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.network.dto.PostDto;

import java.util.Date;
import java.util.List;

public class Post {
    private int id;
    private String text;
    private int authorId;
    private Date createdAt;
    private Date updatedAt;
    private List<String> attachmentNames;

    public Post(PostDto postDto) {
        this.id = postDto.getId();
        this.text = postDto.getText();
        this.authorId = postDto.getAuthorId();
        this.createdAt = postDto.getCreatedAt();
        this.updatedAt = postDto.getUpdatedAt();
        this.attachmentNames = postDto.getAttachmentNames();
    }

    public Post(int id, String text, int authorId, Date createdAt, Date updatedAt, List<String> attachmentNames) {
        this.id = id;
        this.text = text;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attachmentNames = attachmentNames;
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
