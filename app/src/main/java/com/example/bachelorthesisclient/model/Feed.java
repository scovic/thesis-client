package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.network.dto.FeedDto;

public class Feed {
    private Post post;
    private User author;

    public Feed() {
    }

    public Feed(FeedDto feedDto) {
        this.post = new Post(feedDto.getPost());
        this.author = new User(feedDto.getAuthor());
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
}
