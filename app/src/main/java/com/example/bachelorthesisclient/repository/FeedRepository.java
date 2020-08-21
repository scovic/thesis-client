package com.example.bachelorthesisclient.repository;

import android.graphics.Bitmap;

import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.model.Location;
import com.example.bachelorthesisclient.model.Post;
import com.example.bachelorthesisclient.network.dto.PostDto;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public interface FeedRepository {
    Single<List<Feed>> getPosts();
    Single<Post> getPost(int id);
    Single<ResponseBody> getFile(int postId, String fileName);
    Single<PostDto> createNewFeed(String content, int authorId, List<Bitmap> bitmaps);
}
