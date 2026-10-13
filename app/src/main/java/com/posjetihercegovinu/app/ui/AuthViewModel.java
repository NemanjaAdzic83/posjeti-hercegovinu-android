package com.posjetihercegovinu.app.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.posjetihercegovinu.app.repository.AuthRepository;

public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> success = new MutableLiveData<>(false);

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }

    // da li je zahtjev u toku
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    // poruka greske
    public LiveData<String> getError() {
        return error;
    }

    // postaje true kad prijava/registracija uspije(ekran se tada zatvara)
    public LiveData<Boolean> getSuccess() {
        return success;
    }

    // Pokrece prijavu, ako je zahtjev vec u toku, ignosrise dupli klik
    public void login(String email, String password){
        if (Boolean.TRUE.equals(loading.getValue())) return;
        start();
        repository.login(email, password, createCallback());
    }

    // Pokrece registraciju
    public void register(String name, String email, String password){
        if (Boolean.TRUE.equals(loading.getValue())) return;
        start();
        repository.register(name, email, password, createCallback());
    }

    // Callback - zajednicki za login i register - gasi kruzic pa javlja uspjeh ili gresku
    private AuthRepository.AuthCallback createCallback() {
        return new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                loading.setValue(false);
                success.setValue(true);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        };
    }

    // Postavljanja stanje ucitavanj i brise staru gresku
    private void start() {
        loading.setValue(true);
        error.setValue(null);
    }

    public void clearError(){
        error.setValue(null);
    }

}
