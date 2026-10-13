package com.posjetihercegovinu.app.model;

// Podaci o korisniku koje vraca server
public class User {

    private Long id;
    private String name;
    private String email;
    private String role;

    // Gett-eri : daju ekranima  podatke o korisniku, setter-e Gson sam upisuje
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
