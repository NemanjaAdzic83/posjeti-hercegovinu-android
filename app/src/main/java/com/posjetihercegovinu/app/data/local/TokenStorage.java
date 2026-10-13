package com.posjetihercegovinu.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;

// Klasa koja sprema token i osnovne  podatke o korisniku u SharedPreferences
// Token mora prezivjeti zatvaranje aplpikacije. Ako ga samo drzimo u memoriji korisnik bi se
// morao prijavljivati svaki put
public class TokenStorage {

    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_ROLE = "user_role";

    private final SharedPreferences prefs;

    // Konstruktor: otvara ili pravi datoteku postavki. getApplicationContext izbjegava
    // curenje memorije jer ne drzi referencu na ekran
    public TokenStorage(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Sprema token i podatke o korisniku nakon uspjesne prijave iili registracije
    // .apply() - upisuje u pozadini , pa ne blokira ekran
    public void save(String token, String name, String email, String role){
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putString(KEY_ROLE, role)
                .apply();
    }

    // Vraca sacuvani token ili null ako korisnik nije priavljen
    public String getToken(){
        return prefs.getString(KEY_TOKEN,null);
    }

    // Vraca ime prijavljenog korisnika ili null
    public String getName(){
        return prefs.getString(KEY_NAME, null);
    }

    // Vracac email prijavljenog korisnika ili null
    public String getEmail(){
        return prefs.getString(KEY_EMAIL, null);
    }

    // Vraca ulogu prijavljenog korisnika (npr "USER" ili "ADMIN") ili null
    public String getRole(){
        return prefs.getString(KEY_ROLE, null);
    }

    // Da li je neko prijavljen, jeste ako postoji token
    public boolean isLoggedIn(){
        return getToken() != null;
    }

    // Odjava - brise sve sacuvane podatke  o prijavi
    public void clear(){
        prefs.edit().clear().apply();
    }







}
