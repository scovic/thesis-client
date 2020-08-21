package com.example.bachelorthesisclient.repository;

import com.example.bachelorthesisclient.model.Settings;
import com.example.bachelorthesisclient.util.SettingsPersistenceUtil;

import io.reactivex.Single;

public class SettingsRepositoryImpl implements SettingsRepository {
    private static SettingsRepository instance;

    public static SettingsRepository getInstance() {
        if (instance == null) {
            instance = new SettingsRepositoryImpl();
        }

        return instance;
    }

    @Override
    public Single<Settings> getSettings() {
        return Single.just(SettingsPersistenceUtil.getSettings());
    }

    @Override
    public Single<Settings> saveSettings(Settings settings) {
        SettingsPersistenceUtil.persistSettings(settings);
        return Single.just(settings);
    }
}
