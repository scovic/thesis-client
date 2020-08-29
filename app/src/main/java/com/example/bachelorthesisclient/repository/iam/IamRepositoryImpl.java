package com.example.bachelorthesisclient.repository.iam;

import com.example.bachelorthesisclient.model.User;
import com.example.bachelorthesisclient.network.api.IamApi;
import com.example.bachelorthesisclient.network.dto.UsersDto;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.RetrofitWrapper;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class IamRepositoryImpl implements IamRepository {
    private IamApi iamApi;

    private static IamRepository instance;

    public static IamRepository getInstance() {
        if (instance == null) {
            instance = new IamRepositoryImpl();
        }

        return instance;
    }

    private IamRepositoryImpl() {
        this.iamApi = RetrofitWrapper.getInstance().create(IamApi.class);
    }

    @Override
    public Single<User> getUser(final int id) {
        return this.iamApi.getUser(getAuthorizationHeaderValue(), id)
                .subscribeOn(Schedulers.io())
                .map(
                        new Function<UsersDto, User>() {
                            @Override
                            public User apply(UsersDto usersDto) throws Exception {
                                if (usersDto.getUsers().size() == 0) {
                                    throw new Exception("No user with provided id " + String.valueOf(id));
                                }

                                return new User(usersDto.getUsers().get(0));
                            }
                        }
                );
    }

    @Override
    public Single<Boolean> updateUser(int id, User user) {
        return this.iamApi.updateUser(
                getAuthorizationHeaderValue(),
                id,
                user.getFirstName(),
                user.getLastName()
        )
                .subscribeOn(Schedulers.io());
    }

    private String getAuthorizationHeaderValue() {
        String token = LoggedInUserPersistenceUtil.getToken();
        return String.format("Bearer %s", token);
    }
}
