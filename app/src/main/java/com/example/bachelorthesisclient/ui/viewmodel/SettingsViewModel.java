package com.example.bachelorthesisclient.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Settings;
import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.iam.IamRepository;
import com.example.bachelorthesisclient.repository.settings.SettingsRepository;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.PushNotificationsWrapper;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SettingsViewModel extends ViewModel {
    private SettingsRepository settingsRepository;
    private IamRepository iamRepository;

    private Settings settings;
    private MutableLiveData<Float> currentDistance;
    private MutableLiveData<Boolean> receiveInfoNotifications;
    private MutableLiveData<User> userDetails;
    private MutableLiveData<Boolean> canUpdate;

    public SettingsViewModel() {
        settingsRepository = (SettingsRepository) RepositoryFactory.get(RepositoryFactory.SETTINGS_REPOSITORY);
        iamRepository = (IamRepository) RepositoryFactory.get(RepositoryFactory.IAM_REPOSITORY);

        currentDistance = new MutableLiveData<>(0.0f);
        receiveInfoNotifications = new MutableLiveData<>(true);

        this.userDetails = new MutableLiveData<>(null);
        this.canUpdate = new MutableLiveData<>(false);

        getSettings();
        getUser();
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

    private void getUser() {
        this.iamRepository.getUser(LoggedInUserPersistenceUtil.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(User user) {
                        setUserDetails(user);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void updateUserDetails(final String firstName, final String lastName) {
        if (checkIfUserDetailsChanged(firstName, lastName)) {
            iamRepository.updateUser(
                    LoggedInUserPersistenceUtil.getUserId(),
                    new User(firstName, lastName)
            )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new SingleObserver<Boolean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Boolean success) {
                                    if (success) {
                                        User user = getUserDetails().getValue();
                                        user.setFirstName(firstName);
                                        user.setLastName(lastName);

                                        setUserDetails(user);
                                        setCanUpdate(false);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            }
                    );
        }

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

        if (receiveInfoNotifications) {
            PushNotificationsWrapper.addInfoInterest();
        } else {
            PushNotificationsWrapper.removeInfoInterest();
        }

        this.settings.setReceiveInfoNotifications(receiveInfoNotifications);
        this.settingsRepository.saveSettings(settings);
    }

    public MutableLiveData<User> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails.postValue(userDetails);
    }

    public MutableLiveData<Boolean> getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate.setValue(canUpdate);
    }

    private boolean checkIfUserDetailsChanged(String fistName, String lastName) {
        User user = getUserDetails().getValue();
        if (user.getFirstName().equals(fistName) && user.getLastName().equals(lastName)) {
            return false;
        }

        return true;
    }
}
