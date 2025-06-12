package com.eazybytes.chatapp.dto;

public class LoginRequest {
    private String username;
    private String password;

    // Default constructor (जरूरी है JSON से mapping के लिए)
    public LoginRequest() {}

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}