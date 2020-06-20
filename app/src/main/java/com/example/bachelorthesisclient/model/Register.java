package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.expection.EmptyFieldException;
import com.example.bachelorthesisclient.expection.PasswordsDontMatchException;

public class Register {
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws EmptyFieldException {
        if (email.length() == 0) {
            throw new EmptyFieldException("email");
        }
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws EmptyFieldException {
        if (firstName.length() == 0) {
            throw new EmptyFieldException("firstName");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws EmptyFieldException {
        if (lastName.length() == 0) {
            throw new EmptyFieldException("lastName");
        }
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password, String retypePassword) throws PasswordsDontMatchException, EmptyFieldException {
        if (password.length() == 0) {
            throw new EmptyFieldException("password");
        }

        if (retypePassword.length() == 0) {
            throw new EmptyFieldException("retypePassword");
        }

        if (!password.equals(retypePassword)) {
            throw new PasswordsDontMatchException();
        }

        this.password = password;

    }
}
