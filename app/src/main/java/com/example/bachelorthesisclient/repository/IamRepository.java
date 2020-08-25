package com.example.bachelorthesisclient.repository;

import com.example.bachelorthesisclient.model.User;

import io.reactivex.Single;

public interface IamRepository {
    Single<User> getUser(int id);
}
