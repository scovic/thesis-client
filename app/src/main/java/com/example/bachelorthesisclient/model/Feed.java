package com.example.bachelorthesisclient.model;

import android.graphics.Bitmap;

import com.example.bachelorthesisclient.network.dto.FeedDto;

import java.util.ArrayList;
import java.util.List;

public class Feed {
    private Post post;
    private User author;
    private List<Bitmap> images;

    public Feed() {
    }

    public Feed(FeedDto feedDto) {
        this.post = new Post(feedDto.getPost());
        this.author = new User(feedDto.getAuthor());
        this.images = new ArrayList<>();
    }

    public Feed(Post post, User author) {
        this.post = post;
        this.author = author;
        this.images = new ArrayList<>();
    }

    public Feed(Post post, User author, List<Bitmap> images) {
        this.post = post;
        this.author = author;
        this.images = images;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }

    public void addBitmap(Bitmap bitmap) {
        this.images.add(bitmap);
    }
}
