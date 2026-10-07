package com.posjetihercegovinu.app.repository;

import com.posjetihercegovinu.app.model.PageResponse;
import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Repository je jedino mjesto koje zna odakle dolaze podaci(server, Room...)
public class PlaceRepository {

    // Interfejs preko kojega repository javlja rezultat onome koji ga je pozvao (ViewModel-u)
    public interface PlacesCallback{
        void onSuccess(List<Place> places);
        void onError(String message);
    }

    public void getPlaces(int page, int size, PlacesCallback callback){
        RetrofitClient.getApiService().getPlaces(page, size)
                .enqueue(new Callback<PageResponse<Place>>() {
                    @Override
                    public void onResponse(Call<PageResponse<Place>> call, Response<PageResponse<Place>> response) {
                        if (response.isSuccessful() && response.body() != null){
                            callback.onSuccess(response.body().getContent());
                        }else {
                            callback.onError("Greska servera (kod " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<PageResponse<Place>> call, Throwable t) {
                        callback.onError("Nema veze sa serverom");
                    }
                });
    }
}
