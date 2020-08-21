package com.example.bachelorthesisclient.ui.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Feed;
import com.example.bachelorthesisclient.repository.FeedRepository;
import com.example.bachelorthesisclient.repository.NotificationRepository;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.wrapper.FusedLocationProviderWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class HomeViewModel extends ViewModel {
    private FeedRepository feedRepository;
    private NotificationRepository notificationRepository;
    private MutableLiveData<List<Feed>> feeds;
    private MutableLiveData<Boolean> loading;

    public HomeViewModel() throws Exception {
        super();
        this.feedRepository = (FeedRepository) RepositoryFactory.get(RepositoryFactory.FEED_REPOSITORY);
        this.notificationRepository = (NotificationRepository) RepositoryFactory.get(RepositoryFactory.NOTIFICATION_REPOSITORY);

        List<Feed> feeds = new ArrayList<>();
        this.feeds = new MutableLiveData<>(feeds);
        this.loading = new MutableLiveData<>(true);

        this.getData();
    }

    public void getData() {
        this.feedRepository.getPosts()
                .subscribe(new SingleObserver<List<Feed>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<Feed> feeds) {
                        setFeeds(feeds);
                        setLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("HomeViewModel", e.getMessage() != null ? e.getMessage() : "Something went wrong");
                    }
                });
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading.setValue(loading);
    }

    public MutableLiveData<List<Feed>> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds.setValue(feeds);
    }

    public void sendWarningNotification() {
        FusedLocationProviderWrapper.getInstance()
                .getLastLocation()
                .flatMap(new Function<Location, SingleSource<Boolean>>() {
                    @Override
                    public SingleSource<Boolean> apply(Location location) throws Exception {
                        double lat = 0;
                        double lon = 0;

                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }

                        return notificationRepository.sendWarningNotification(
                                new com.example.bachelorthesisclient.model.Location(lat, lon)
                        );
                    }
                })
                .subscribe();
    }

}
