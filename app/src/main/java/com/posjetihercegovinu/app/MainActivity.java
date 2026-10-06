package com.posjetihercegovinu.app;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.posjetihercegovinu.app.model.PageResponse;
import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RetrofitClient.getApiService().getPlaces(0,10).enqueue(new Callback<PageResponse<Place>>() {
            @Override
            public void onResponse(Call<PageResponse<Place>> call, Response<PageResponse<Place>> response) {
                if (response.isSuccessful() && response.body() != null){
                    Log.d("API_TEST", "broj mjesta: " + response.body().getContent());
                }  else {
                    Log.e("API_TEST","Greska, status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PageResponse<Place>> call, Throwable t) {
                Log.e("API_TEST", "Neuspjesno: " + t.getMessage());
            }
        });


    }
}