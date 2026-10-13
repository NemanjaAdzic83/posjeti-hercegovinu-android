package com.posjetihercegovinu.app.model;

// Odgovor na login i register ( token + korisnik)
public class AuthResponse {

    private String token;
    private User user;

    // Vraca token koji cemo slati uz svaki zasticeni zahtjev
    public String getToken() {
        return token;
    }

    // Vraca podatke o prijavljenom korisniku
    public User getUser() {
        return user;
    }
}
