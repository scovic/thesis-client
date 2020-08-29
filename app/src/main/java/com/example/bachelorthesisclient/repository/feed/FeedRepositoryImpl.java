package com.example.bachelorthesisclient.repository.feed;

import android.graphics.Bitmap;

import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.model.Post;
import com.example.bachelorthesisclient.network.api.FeedApi;
import com.example.bachelorthesisclient.network.dto.FeedDto;
import com.example.bachelorthesisclient.network.dto.FeedsDto;
import com.example.bachelorthesisclient.network.dto.PostDto;
import com.example.bachelorthesisclient.util.ExternalStorageUtil;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.RetrofitWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class FeedRepositoryImpl implements FeedRepository {
    private FeedApi feedApi;

    private static FeedRepository instance;

    public static FeedRepository getInstance() {
        if (instance == null) {
            instance = new FeedRepositoryImpl();
        }

        return instance;
    }

    private FeedRepositoryImpl() {
        this.feedApi = RetrofitWrapper.getInstance().create(FeedApi.class);
    }

    @Override
    public Single<List<Feed>> getPosts() {
        return this.feedApi.getFeeds(this.getAuthorizationHeaderValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<FeedsDto, List<Feed>>() {
                    @Override
                    public List<Feed> apply(FeedsDto feedsDtos) throws Exception {
                        List<Feed> returnList = new ArrayList<>();

                        for (FeedDto dto : feedsDtos.getPosts()) {
                            returnList.add(new Feed(dto));
                        }

                        return returnList;
                    }
                });
    }

    @Override
    public Single<Post> getPost(int id) {
        return feedApi.getPost(this.getAuthorizationHeaderValue(), id)
                .subscribeOn(Schedulers.io())
                .map(new Function<PostDto, Post>() {
                    @Override
                    public Post apply(PostDto postDto) throws Exception {
                        return new Post(postDto);
                    }
                });
    }

    @Override
    public Single<ResponseBody> getFile(int postId, String fileName) {

        return feedApi.getFile(this.getAuthorizationHeaderValue(), postId, fileName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<PostDto> createNewFeed(String content, int authorId, List<Bitmap> bitmaps) {
        final List<String> tempFilesPaths = new ArrayList<>();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("text", content);
        builder.addFormDataPart("authorId", String.valueOf(authorId));

        for (Bitmap bitmap : bitmaps) {
            String absolutePath = ExternalStorageUtil.saveBitmap(bitmap);
            tempFilesPaths.add(absolutePath);
            File file = new File(absolutePath);

            RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("files", file.getName(), requestImage);

        }

        MultipartBody requestBody = builder.build();

        return feedApi.createFeed(this.getAuthorizationHeaderValue(), requestBody)
                .subscribeOn(Schedulers.io())
                .map(new Function<PostDto, PostDto>() {
                    @Override
                    public PostDto apply(PostDto postDto) throws Exception {
                        for (String path : tempFilesPaths) {
                            ExternalStorageUtil.deleteFile(path);
                        }

                        return postDto;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> deleteFeed(Feed feed) {
        return feedApi.deleteFeed(
                getAuthorizationHeaderValue(),
                feed.getPost().getId()
        ).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Boolean> updateFeed(Feed feed, List<Bitmap> bitmaps) {
        final List<String> tempFilesPaths = new ArrayList<>();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("text", feed.getPost().getText());
        builder.addFormDataPart("authorId", String.valueOf(feed.getPost().getAuthorId()));
        builder.addFormDataPart("id", String.valueOf(feed.getPost().getId()));

        for (Bitmap bitmap : bitmaps) {
            String absolutePath = ExternalStorageUtil.saveBitmap(bitmap);
            tempFilesPaths.add(absolutePath);
            File file = new File(absolutePath);

            RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("files", file.getName(), requestImage);

        }

        MultipartBody requestBody = builder.build();

        return feedApi.updateFeed(
                getAuthorizationHeaderValue(),
                requestBody
        ).subscribeOn(Schedulers.io())
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        for (String path : tempFilesPaths) {
                            ExternalStorageUtil.deleteFile(path);
                        }

                        return aBoolean;
                    }
                });
    }

    private String getAuthorizationHeaderValue() {
        String token = LoggedInUserPersistenceUtil.getToken();
        return String.format("Bearer %s", token);
    }
}
