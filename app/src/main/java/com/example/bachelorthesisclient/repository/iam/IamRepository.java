package com.example.bachelorthesisclient.repository.iam;

import com.example.bachelorthesisclient.model.User;

import io.reactivex.Single;

public interface IamRepository {
    Single<User> getUser(int id);
    Single<Boolean> updateUser(int id, User user);
}
