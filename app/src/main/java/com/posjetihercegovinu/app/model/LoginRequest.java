package com.posjetihercegovinu.app.model;

// Ono sto saljemo pri prijavi
public class LoginRequest {
    private final String email;
    private final String password;


    // konstruktor pamti email i lozinku koju ce Gson pretvoriti u JSON
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
