package com.example.bachelorthesisclient.service;

import android.content.Intent;
import android.location.Location;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.config.Values;
import com.example.bachelorthesisclient.model.NotificationData;
import com.example.bachelorthesisclient.ui.activity.MapActivity;
import com.example.bachelorthesisclient.util.AppContext;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.util.SettingsPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;
import com.example.bachelorthesisclient.wrapper.NotificationsWrapper;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.fcm.MessagingService;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.util.GeoPoint;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NotificationsMessagingService extends MessagingService {
    public static final String INFO_TAG = "info";
    public static final String WARNING_TAG = "warning";

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        AppContext.saveAppContext(getApplicationContext());

        if (LoggedInUserPersistenceUtil.isLoggedIn()) {
            NotificationData data = new NotificationData(remoteMessage.getData());
            int senderId = data.getSender();

            if (LoggedInUserPersistenceUtil.getUser().isSameUser(senderId)) {
                return;
            }

            handleNotification(data);
        }
    }

    private void handleNotification(NotificationData data) {
        FusedLocationProviderWrapper.getInstance()
                .getLastLocation()
                .subscribe(handleGetLastLocationSubscribe(data));
    }

    private SingleObserver<android.location.Location> handleGetLastLocationSubscribe(final NotificationData data) {
        return new SingleObserver<android.location.Location>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(android.location.Location location) {
                if (data.isInfoNotification() && SettingsPersistenceUtil.canReceiveInfoNotifications()) {
                    handleInfoNotification(data, location);
                } else {
                    handleWarningNotification(data, location);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    private void handleInfoNotification(@NotNull NotificationData notificationData, @NotNull Location location) {
        android.location.Location senderLocation = mapToAndroidLocation(
                notificationData.getLocation(),
                "Sender Location"
        );
        double distance = location.distanceTo(senderLocation);

        if (distance <= SettingsPersistenceUtil.getSettings().getCurrentDistance()) {
            showInfoNotification(notificationData);
        }
    }

    private void handleWarningNotification(NotificationData data, @NotNull Location location) {
        android.location.Location eventLocation = mapToAndroidLocation(
                Values.eventLocation,
                "Event location"
        );
        double distance = location.distanceTo(eventLocation);

        if (distance <= Values.DISTANCE_WARNING_NOTIFICATIONS) {
            showWarningNotification(data);
        }
    }

    private void showInfoNotification(NotificationData data) {
        Intent i = this.getIntent(data);
        i.putExtra("postId", data.getPostId());
        i.putExtra("tag", INFO_TAG);

        NotificationsWrapper.getInstance()
                .setTitle(data.getTitle())
                .setContent(data.getBody())
                .setOnTapAction(i)
                .setAutoCancel()
                .createNotification();
    }

    private void showWarningNotification(NotificationData data) {
        Intent i = getIntent(data);
        i.putExtra("tag", WARNING_TAG);

        NotificationsWrapper.getInstance()
                .setTitle(data.getTitle())
                .setContent(data.getBody())
                .setIcon(R.drawable.icon_danger)
                .setOnTapAction(i)
                .createNotification();
    }

    private Intent getIntent(NotificationData data) {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("latitude", data.getLocation().getLatitude());
        i.putExtra("longitude", data.getLocation().getLongitude());
        return i;
    }

    private android.location.Location mapToAndroidLocation(com.example.bachelorthesisclient.model.Location location, String providerName) {
        android.location.Location resultLocation = new android.location.Location(providerName);

        resultLocation.setLatitude(location.getLatitude());
        resultLocation.setLongitude(location.getLongitude());

        return resultLocation;
    }

    private android.location.Location mapToAndroidLocation(GeoPoint point, String providerName) {
        android.location.Location resultLocation = new android.location.Location(providerName);

        resultLocation.setLatitude(point.getLatitude());
        resultLocation.setLongitude(point.getLongitude());

        return resultLocation;
    }


}

