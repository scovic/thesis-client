package com.example.bachelorthesisclient.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bachelorthesisclient.wrapper.SharedPreferenceWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferenceWrapper.createInstance(this);
        String loggedUserDetails = null;

        try {
            loggedUserDetails = (String) SharedPreferenceWrapper
                    .getInstance()
                    .get(LoginActivity.LOGGED_USER_KEY, "");

        } catch (Exception ex) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        Intent i;

        if (!loggedUserDetails.isEmpty()) {
            i = new Intent(this, HomeActivity.class);
        } else {
            i = new Intent(this, LoginActivity.class);
        }

        startActivity(i);
        finish();
    }
}
