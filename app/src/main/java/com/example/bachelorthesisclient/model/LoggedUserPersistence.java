package com.example.bachelorthesisclient.model;

public class LoggedUserPersistence {
    private String token;
    private String email;

    public LoggedUserPersistence(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}
