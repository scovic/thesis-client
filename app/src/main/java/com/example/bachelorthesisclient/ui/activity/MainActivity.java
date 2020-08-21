package com.example.bachelorthesisclient.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bachelorthesisclient.util.AppContext;
import com.example.bachelorthesisclient.util.SettingsPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.PicassoWrapper;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.PushNotificationsWrapper;
import com.example.bachelorthesisclient.wrapper.SharedPreferenceWrapper;
import com.pusher.pushnotifications.PushNotifications;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppContext.saveAppContext(getApplicationContext());
        PushNotificationsWrapper.init();

        SharedPreferenceWrapper.createInstance(this);
        PicassoWrapper.createInstance(this);

        this.determineFirstActivity();
    }

    private void determineFirstActivity() {
        Intent i;

        if (LoggedInUserPersistenceUtil.isLoggedIn()) {
            i = new Intent(this, HomeActivity.class);
        } else {
            i = new Intent(this, LoginActivity.class);
        }

        startActivity(i);
        finish();
    }
}
