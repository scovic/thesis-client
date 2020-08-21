package com.example.bachelorthesisclient.service;

import android.content.Intent;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.model.NotificationData;
import com.example.bachelorthesisclient.ui.activity.FeedPreviewActivity;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.util.SettingsPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.NotificationsWrapper;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;

import org.jetbrains.annotations.NotNull;

public class NotificationsMessagingService extends MessagingService {
    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {

        if (LoggedInUserPersistenceUtil.isLoggedIn()) {
            NotificationData data = new NotificationData(remoteMessage.getData());

            int senderId = data.getSender();

            if (LoggedInUserPersistenceUtil.getUser().isSameUser(senderId)) {
                return;
            }

            if (data.isInfoNotification() && SettingsPersistenceUtil.canReceiveInfoNotifications()) {
                Intent i = new Intent(this, FeedPreviewActivity.class);
                i.putExtra("postId", data.getPostId());

                NotificationsWrapper.getInstance()
                        .setTitle(data.getTitle())
                        .setContent(data.getBody())
                        .setOnTapAction(i)
                        .createNotification();
            } else {
                NotificationsWrapper.getInstance()
                        .setTitle(data.getTitle())
                        .setContent(data.getBody())
                        .setIcon(R.drawable.icon_danger)
                        .createNotification();
            }
        }
    }
}

