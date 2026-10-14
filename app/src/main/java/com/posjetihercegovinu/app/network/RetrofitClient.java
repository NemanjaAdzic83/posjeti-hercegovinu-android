package com.posjetihercegovinu.app.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.posjetihercegovinu.app.BuildConfig;
import com.posjetihercegovinu.app.data.local.TokenStorage;

public class RetrofitClient {

    // Ovo je IP adresa pokretanje na za emulatoru
    private static final String BASE_URL = BuildConfig.API_BASE_URL;



    // jedna zajednicka instanca za cijelu apikaciju
    private static ApiService apiService;
    private static TokenStorage tokenStorage;

    // Poziva se jednom iz PosjetiApp. Pamti TokenStorage za interceptor
    public static void init(Context context){
        tokenStorage = new TokenStorage(context);
    }

    // Vraca zajednicki ApiService, pri prvom pozivu ga sastavlja
    public static ApiService getApiService(){
        if (apiService == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }

    // Pravi interceptor - za svaki zahtjev provjeri ima li sacuvanog tokena
    // i ako ga ima , doda zaglavlje "Authorization: Bearer <token>"
    private static okhttp3.Interceptor authInterceptor(){
        return chain -> {
            Request original = chain.request();
            String token = tokenStorage != null ? tokenStorage.getToken() : null;
            if (token == null){
                return chain.proceed(original);
            }
            Request withToken = original.newBuilder()
                    .header("Authorization","Bearer " + token)
                    .build();

            return chain.proceed(withToken);
        };
    }



}
