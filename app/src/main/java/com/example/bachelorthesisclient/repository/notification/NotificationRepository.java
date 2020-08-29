package com.example.bachelorthesisclient.repository.notification;


import com.example.bachelorthesisclient.model.Location;
import com.example.bachelorthesisclient.network.dto.PostDto;

import io.reactivex.Single;

public interface NotificationRepository {
    Single<Boolean> sendInfoNotification(PostDto postDto, Location location);
    Single<Boolean> sendWarningNotification(Location location);
}
