package com.example.bachelorthesisclient.network.dto;

import com.example.bachelorthesisclient.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedsDto {
    List<FeedDto> posts;

    public FeedsDto(List<FeedDto> posts) {
        this.posts = posts;
    }

    public FeedsDto(List<PostDto> posts, List<UserDto> users) {
        this.posts = new ArrayList<>();

        for (int i = 0; i < posts.size(); i++) {
            this.posts.add(new FeedDto(
                    posts.get(i),
                    users.get(i)
            ));
        }
    }

    public List<FeedDto> getPosts() {
        return posts;
    }

    public void setPosts(List<FeedDto> posts) {
        this.posts = posts;
    }
}
