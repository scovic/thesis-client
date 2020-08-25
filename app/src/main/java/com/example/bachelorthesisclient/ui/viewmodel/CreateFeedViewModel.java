package com.example.bachelorthesisclient.ui.viewmodel;

import android.graphics.Bitmap;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.network.dto.PostDto;
import com.example.bachelorthesisclient.repository.FeedRepository;
import com.example.bachelorthesisclient.repository.NotificationRepository;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class CreateFeedViewModel extends ViewModel {
    private FeedRepository feedRepository;
    private NotificationRepository notificationRepository;

    private MutableLiveData<String> content;
    private MutableLiveData<List<Bitmap>> images;
    private MutableLiveData<Boolean> sendNotification;
    private MutableLiveData<Boolean> emptyContentField;

    private MutableLiveData<Boolean> successfulCreated;

    public CreateFeedViewModel() {
        super();

        this.feedRepository = (FeedRepository) RepositoryFactory.get(RepositoryFactory.FEED_REPOSITORY);
        this.notificationRepository = (NotificationRepository) RepositoryFactory.get(RepositoryFactory.NOTIFICATION_REPOSITORY);

        List<Bitmap> imagesDefaultValue = new ArrayList<Bitmap>();
        this.images = new MutableLiveData<>(imagesDefaultValue);
        this.content = new MutableLiveData<>("");
        this.sendNotification = new MutableLiveData<>(false);
        this.successfulCreated = new MutableLiveData<>(false);
        this.emptyContentField = new MutableLiveData<>(false);
    }

    public void createFeed() {
        if (!content.getValue().isEmpty()) {
            setEmptyContentField(false);
            this.handleCreateFeed();
        } else {
            setEmptyContentField(true);
        }
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

            }
        };
    }

    public void addImage(Bitmap image) {
        List<Bitmap> images = this.images.getValue();
        images.add(image);
        this.images.setValue(images);
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
}
