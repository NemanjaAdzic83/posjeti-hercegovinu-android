package com.posjetihercegovinu.app.repository;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.posjetihercegovinu.app.data.local.TokenStorage;
import com.posjetihercegovinu.app.model.AuthResponse;
import com.posjetihercegovinu.app.model.LoginRequest;
import com.posjetihercegovinu.app.model.RegisterRequest;
import com.posjetihercegovinu.app.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Jedino mjesto koje zna kako se poziva login i register i kako se cita poruka greske
public class AuthRepository {

    // Interfejs preko kojeg javljamo rezultat prijave/registracije onome ko je pozva
    public interface AuthCallback{
        void onSuccess();
        void onError(String message);
    }

    private final TokenStorage tokenStorage;

    // Konstruktor pravi TokenStorage kojim cemo sacuvati token nakon prijave
    public AuthRepository(Context context) {
        this.tokenStorage = new TokenStorage(context);
    }

    // Prijava - salje email i lozinku. Ako uspije, sprema token i javlja uspjeh,
    // ako ne uspije onda javlja poruku greske
    public void login(String email, String password, AuthCallback callback){
        RetrofitClient.getApiService().login(new LoginRequest(email,password))
                .enqueue(createCallback(callback));
    }

    // Registracija - salje ime, email i lozinku. Bacjkend odmah vraca i token, pa je
    // korisnik nakon registracije vec prijavljen
    public void register(String name, String email, String password, AuthCallback callback){
        RetrofitClient.getApiService().register(new RegisterRequest(name, email, password))
                .enqueue(createCallback(callback));
    }

    // Odjava - brise sacuvani token i podatke
    public void logout(){
        tokenStorage.clear();
    }

    // Da li  je korisnik trenutno prijavljen
    public boolean isLoggedIn(){
        return tokenStorage.isLoggedIn();
    }

    // Pomocna metoda pravi Callback koji je isti za login i register
    // Uspjeh- sprema token i podatke, Greska- cita poruku iz JSON servera
    private Callback<AuthResponse> createCallback(AuthCallback callback) {
        return new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    AuthResponse body = response.body();
                    tokenStorage.save(
                            body.getToken(),
                            body.getUser().getName(),
                            body.getUser().getEmail(),
                            body.getUser().getRole());
                    callback.onSuccess();
                } else {
                    callback.onError(readErrorMessage(response));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError("Nema veze sa serverom");
            }
        };
    }

    // Cita polje "message" iz JSON greske, ako to  ne uspije vraca opstu poruku
    private String readErrorMessage(Response<?> response) {
        try {
            if (response.errorBody() != null){
                String json = response.errorBody().string();
                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                if (obj.has("message")){
                    return obj.get("message").getAsString();
                }
            }
        }catch (Exception ignored){

        }
        return "Greska servera (kod " + response.code() + ")";
    }


}
