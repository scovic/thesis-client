package com.example.bachelorthesisclient.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Settings;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.SettingsRepository;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SettingsViewModel extends ViewModel {
    private SettingsRepository settingsRepository;

    private Settings settings;
    private MutableLiveData<Float> currentDistance;
    private MutableLiveData<Boolean> receiveInfoNotifications;

    public SettingsViewModel() {
        settingsRepository = (SettingsRepository) RepositoryFactory.get(RepositoryFactory.SETTINGS_REPOSITORY);

        currentDistance = new MutableLiveData<>(0.0f);
        receiveInfoNotifications = new MutableLiveData<>(true);
        getSettings();
    }

    private void getSettings() {
        this.settingsRepository.getSettings()
                .subscribe(new SingleObserver<Settings>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Settings persistedSettings) {
                        currentDistance.setValue(persistedSettings.getCurrentDistance());
                        receiveInfoNotifications.setValue(persistedSettings.isReceiveInfoNotifications());
                        settings = persistedSettings;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public MutableLiveData<Float> getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(Float currentDistance) {
        this.currentDistance.setValue(currentDistance);

        this.settings.setCurrentDistance(currentDistance);
        this.settingsRepository.saveSettings(settings).subscribe();
    }

    public MutableLiveData<Boolean> getReceiveInfoNotifications() {
        return receiveInfoNotifications;
    }

    public void setReceiveInfoNotifications(Boolean receiveInfoNotifications) {
        this.receiveInfoNotifications.setValue(receiveInfoNotifications);

        this.settings.setReceiveInfoNotifications(receiveInfoNotifications);
        this.settingsRepository.saveSettings(settings);
    }
}
