package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.network.dto.UserDto;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.email = userDto.getEmail();
        this.firstName = userDto.getFirstName();
        this.lastName = userDto.getLastName();
    }

    public User(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean isSameUser(int id) {
        return this.id == id;
    }

    public boolean isSameUser(User user) {
        return this.id == user.getId();
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
