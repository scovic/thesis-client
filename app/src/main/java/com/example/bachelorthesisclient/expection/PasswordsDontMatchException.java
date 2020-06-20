package com.example.bachelorthesisclient.expection;

public class PasswordsDontMatchException extends Exception {
    public PasswordsDontMatchException() {
        super("Passwords don't match");
    }
}
