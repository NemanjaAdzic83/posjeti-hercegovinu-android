package com.posjetihercegovinu.app;

import android.app.Application;

import com.posjetihercegovinu.app.network.RetrofitClient;

public class PosjetiApp extends Application {

    // Poziva se pri pokretanju aplikacije. Predajemo kontekst RetrofitClient-u da bi
    // interceptor mogao citati token iz TokenStorage

    @Override
    public void onCreate(){
        super.onCreate();
        RetrofitClient.init(this);
    }
}
