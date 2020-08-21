package com.example.bachelorthesisclient.service;

import com.example.bachelorthesisclient.model.Register;
import com.example.bachelorthesisclient.model.RegisterResponse;
import com.example.bachelorthesisclient.wrapper.RetrofitWrapper;
import com.example.bachelorthesisclient.network.api.AuthApi;
import com.example.bachelorthesisclient.model.BasicLogin;
import com.example.bachelorthesisclient.model.BasicLoginResponse;
import com.example.bachelorthesisclient.model.LoggedUserPersistence;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthService {
    private AuthApi authApi;

    private static AuthService instance;

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }

        return instance;
    }

    private AuthService() {
        this.authApi = RetrofitWrapper.getInstance().create(AuthApi.class);
    }

    public Single<LoggedUserPersistence> login(final BasicLogin login) {
        return authApi.login(login.getEmail(), login.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<BasicLoginResponse, LoggedUserPersistence>() {

                    @Override
                    public LoggedUserPersistence apply(BasicLoginResponse basicLoginResponse) throws Exception {
                        return new LoggedUserPersistence(basicLoginResponse.getToken(), login.getEmail());
                    }
                });
    }

    public Single<RegisterResponse> registerUser(final Register register) {
        return authApi.register(
            register.getEmail(),
            register.getFirstName(),
            register.getLastName(),
            register.getPassword()
        )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }
}
