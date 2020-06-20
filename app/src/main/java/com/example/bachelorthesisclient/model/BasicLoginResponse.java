package com.example.bachelorthesisclient.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicLoginResponse {
    @Expose
    @SerializedName("token")
    private String token;

    public BasicLoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
