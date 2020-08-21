package com.example.bachelorthesisclient.network.dto;

import android.graphics.Bitmap;

public class FeedDto {
    private PostDto post;
    private UserDto author;
    private Bitmap[] images;

    public FeedDto(PostDto post, UserDto author) {
        this.post = post;
        this.author = author;
    }

    public PostDto getPost() {
        return post;
    }

    public void setPost(PostDto post) {
        this.post = post;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }

    public Bitmap[] getImages() {
        return images;
    }

    public void setImages(Bitmap[] images) {
        this.images = images;
    }
}
