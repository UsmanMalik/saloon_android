package com.saloon.android.bluecactus.app.Models;

/**
 * Created by usman on 10/8/16.
 */
public class User {

    private String first_name;
    private String last_name;
    private String email;
    private String password;

    public User() {
    }

    public User(String first_name, String email, String password) {
        this.first_name = first_name;
//        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }



}
