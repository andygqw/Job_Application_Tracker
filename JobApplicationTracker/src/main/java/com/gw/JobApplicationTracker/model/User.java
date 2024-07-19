package com.gw.JobApplicationTracker.model;

import java.time.LocalDate;


public class User {


    private long id;
    private String email;
    private String password;
    private LocalDate registration_date;
    private String additional_info;
    private String username;
    
    public User(long id, String password, String username) {
        this.id = id;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword() {
        return password;
    }
}