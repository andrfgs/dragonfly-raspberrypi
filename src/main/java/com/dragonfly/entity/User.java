package com.dragonfly.entity;

public class User {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public User() { }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
