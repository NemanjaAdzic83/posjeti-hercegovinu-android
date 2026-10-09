package com.posjetihercegovinu.app.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.posjetihercegovinu.app.BuildConfig;

public class RetrofitClient {

    // Ovo je IP adresa pokretanje na za emulatoru
    private static final String BASE_URL = BuildConfig.API_BASE_URL;



    // jedna zajednicka instanca za cijelu apikaciju
    private static ApiService apiService;

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



}
