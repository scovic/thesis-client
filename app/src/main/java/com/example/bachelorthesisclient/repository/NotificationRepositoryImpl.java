package com.example.bachelorthesisclient.repository;

import com.example.bachelorthesisclient.model.Location;
import com.example.bachelorthesisclient.model.NotificationData;
import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.network.api.NotificationApi;
import com.example.bachelorthesisclient.network.dto.NotificationDto;
import com.example.bachelorthesisclient.network.dto.PostDto;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.RetrofitWrapper;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotificationRepositoryImpl implements NotificationRepository {
    private NotificationApi notificationApi;

    private static NotificationRepository instance;

    public static NotificationRepository getInstance() {
        if (instance == null) {
            instance = new NotificationRepositoryImpl();
        }
        return instance;
    }

    private NotificationRepositoryImpl() {
        this.notificationApi = RetrofitWrapper.getInstance().create(NotificationApi.class);
    }

    @Override
    public Single<Boolean> sendInfoNotification(PostDto postDto, Location location) {
        NotificationData data = new NotificationData();

        User loggedInUser = LoggedInUserPersistenceUtil.getUser();
        String title = String.format("%s %s made a feed", loggedInUser.getFirstName(), loggedInUser.getLastName());
        String body = postDto.getText();

        data.setTitle(title);
        data.setBody(body);
        data.setSender(loggedInUser.getId());
        data.setLocation(location);
        data.setPostId(postDto.getId());
        data.setInfoTag();

        return notificationApi.sendInfoNotification(getAuthorizationHeaderValue(), new NotificationDto(data.build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> sendWarningNotification(Location location) {
        NotificationData data = new NotificationData()
                .setTitle("WARNING!")
                .setBody("Please follow the path to nearest exit")
                .setWarningTag()
                .setLocation(location)
                .setSender(LoggedInUserPersistenceUtil.getUserId());

        return notificationApi.sendInfoNotification(getAuthorizationHeaderValue(), new NotificationDto(data.build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private String getAuthorizationHeaderValue() {
        String token = LoggedInUserPersistenceUtil.getToken();
        return String.format("Bearer %s", token);
    }
}
