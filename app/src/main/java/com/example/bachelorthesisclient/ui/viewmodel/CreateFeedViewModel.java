package com.example.bachelorthesisclient.ui.viewmodel;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.model.Post;
import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.network.dto.PostDto;
import com.example.bachelorthesisclient.repository.feed.FeedRepository;
import com.example.bachelorthesisclient.repository.iam.IamRepository;
import com.example.bachelorthesisclient.repository.notification.NotificationRepository;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.util.ImageUtil;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.util.MapUtil;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CreateFeedViewModel extends ViewModel {
    private FeedRepository feedRepository;
    private NotificationRepository notificationRepository;

    private MutableLiveData<String> content;
    private MutableLiveData<List<Bitmap>> images;
    private MutableLiveData<Boolean> sendNotification;
    private MutableLiveData<Boolean> emptyContentField;
    private MutableLiveData<Boolean> successfulCreated;
    private MutableLiveData<Feed> feedToEdit;

    public CreateFeedViewModel() {
        super();

        this.feedRepository = (FeedRepository) RepositoryFactory.get(RepositoryFactory.FEED_REPOSITORY);
        this.notificationRepository = (NotificationRepository) RepositoryFactory.get(RepositoryFactory.NOTIFICATION_REPOSITORY);

        List<Bitmap> imagesDefaultValue = new ArrayList<>();
        this.images = new MutableLiveData<>(imagesDefaultValue);
        this.content = new MutableLiveData<>("");
        this.sendNotification = new MutableLiveData<>(false);
        this.successfulCreated = new MutableLiveData<>(false);
        this.emptyContentField = new MutableLiveData<>(false);
        this.feedToEdit = new MutableLiveData<>(null);
    }

    public void createFeed() {
        if (!content.getValue().isEmpty()) {
            setEmptyContentField(false);
            this.handleCreateFeed();
        } else {
            setEmptyContentField(true);
        }
    }

    public void updateFeed() {
        if (!content.getValue().isEmpty()) {
            setEmptyContentField(false);
            handleUpdateFeed();
        } else {
            setEmptyContentField(true);
        }
    }

    public void loadFeedDetails(final int postId) {
        feedRepository.getPost(postId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Post>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Post post) {
                        Feed feed = new Feed();
                        feed.setPost(post);
                        setFeedToEdit(feed);
                        loadImages(post.getAttachmentNames(), postId);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void handleCreateFeed() {
        int authorId = LoggedInUserPersistenceUtil.getUserId();

        Single<PostDto> createFeedSingle = feedRepository.createNewFeed(
                this.content.getValue(),
                authorId,
                this.images.getValue()
        );

        if (getSendNotification().getValue()) {
            Single<Location> getCurrentLocationSingle = FusedLocationProviderWrapper
                    .getInstance()
                    .getLastLocation();

            Single.zip(createFeedSingle, getCurrentLocationSingle, this.handleZipResult())
                    .flatMap(
                            new Function<Single<Boolean>, Single<Boolean>>() {
                                @Override
                                public Single<Boolean> apply(Single<Boolean> booleanSingle) throws Exception {
                                    return booleanSingle;
                                }
                            }
                    )
                    .subscribe(this.handleCreateFeedSubscribe());

        } else {
            createFeedSingle.subscribe(this.handleCreateFeedSubscribe());
        }
    }

    private void handleUpdateFeed() {
        Feed feed = getFeedToEdit().getValue();
        feed.getPost().setText(this.content.getValue());
        feedRepository.updateFeed(
                feed,
                this.images.getValue()
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleCreateFeedSubscribe());
    }

    private BiFunction<PostDto, Location, Single<Boolean>> handleZipResult() {
        return new BiFunction<PostDto, Location, Single<Boolean>>() {
            @Override
            public Single<Boolean> apply(PostDto postDto, Location location) throws Exception {
                double lat = 0;
                double lon = 0;

                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }

                return notificationRepository.sendInfoNotification(
                        postDto,
                        new com.example.bachelorthesisclient.model.Location(lat, lon)
                );
            }
        };
    }

    private SingleObserver<Object> handleCreateFeedSubscribe() {
        return new SingleObserver<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Object o) {
                setSuccessfulCreated(true);
            }

            @Override
            public void onError(Throwable e) {
                int a = 5;
            }
        };
    }

    public void addImage(Bitmap image) {
        List<Bitmap> images = this.images.getValue();
        images.add(image);
        setImages(images);
    }

    public void setImages(List<Bitmap> bitmaps) {
        this.images.setValue(bitmaps);
    }

    public void setSuccessfulCreated(boolean successfulCreated) {
        this.successfulCreated.setValue(successfulCreated);
    }

    public void setContent(String content) {
        this.content.setValue(content);
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification.setValue(sendNotification);
    }

    public void setEmptyContentField(boolean emptyContentField) {
        this.emptyContentField.setValue(emptyContentField);
    }

    public void setFeedToEdit(Feed feedToEdit) {
        this.feedToEdit.setValue(feedToEdit);
    }

    public MutableLiveData<String> getContent() {
        return content;
    }

    public MutableLiveData<List<Bitmap>> getImages() {
        return images;
    }

    public MutableLiveData<Boolean> getSuccessfulCreated() {
        return successfulCreated;
    }

    public MutableLiveData<Boolean> getSendNotification() {
        return sendNotification;
    }

    public MutableLiveData<Boolean> getEmptyContentField() {
        return emptyContentField;
    }

    public MutableLiveData<Feed> getFeedToEdit() {
        return feedToEdit;
    }

    public void loadImages(final List<String> imageNames, final int postId) {
        for (String imageName : imageNames) {
            PicassoWrapper.getInstance()
                    .loadImage(
                            ImageUtil.makeImageUrl(postId, imageName),
                            new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    addImage(bitmap);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            }
                    );
        }
    }

}
