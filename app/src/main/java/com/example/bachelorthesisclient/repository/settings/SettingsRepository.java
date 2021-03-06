package com.example.bachelorthesisclient.repository.settings;

import com.example.bachelorthesisclient.model.Settings;

import io.reactivex.Single;

public interface SettingsRepository {
    Single<Settings> getSettings();
    Single<Settings> saveSettings(Settings settings);
}
