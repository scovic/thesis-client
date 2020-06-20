package com.example.bachelorthesisclient.expection;

public class EmptyFieldException extends Exception {
    public EmptyFieldException(String fieldName) {
        super(String.format("Field %s must not be empty!", fieldName));
    }
}
