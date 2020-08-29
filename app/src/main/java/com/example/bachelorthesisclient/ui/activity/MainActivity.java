package com.example.bachelorthesisclient.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.eventdetails.EventDetailsRepository;
import com.example.bachelorthesisclient.util.AppContext;
import com.example.bachelorthesisclient.util.EventDetailsPersistenceUtil;
import com.example.bachelorthesisclient.util.SettingsPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.PushNotificationsWrapper;
import com.example.bachelorthesisclient.wrapper.SharedPreferenceWrapper;
import com.pusher.pushnotifications.PushNotifications;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContext.saveAppContext(getApplicationContext());
        PushNotificationsWrapper.init();

        SharedPreferenceWrapper.createInstance(this);
        this.determineFirstActivity();
    }

    private void determineFirstActivity() {
        Intent i;

        if (LoggedInUserPersistenceUtil.isLoggedIn()) {
            loadEventDetails();
            i = new Intent(this, HomeActivity.class);
        } else {
            clearEventDetails();
            i = new Intent(this, LoginActivity.class);
        }

        startActivity(i);
        finish();
    }

    private void loadEventDetails() {
        if (EventDetailsPersistenceUtil.getEventDetails() == null) {
            ((EventDetailsRepository) RepositoryFactory.get(RepositoryFactory.EVENT_DETAILS_REPOSITORY))
                    .getEventDetails()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<EventDetails>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(EventDetails eventDetails) {
                            EventDetailsPersistenceUtil.putEventDetails(eventDetails);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    private void clearEventDetails() {
        if (EventDetailsPersistenceUtil.getEventDetails() != null) {
            EventDetailsPersistenceUtil.clear();
        }
    }
}
