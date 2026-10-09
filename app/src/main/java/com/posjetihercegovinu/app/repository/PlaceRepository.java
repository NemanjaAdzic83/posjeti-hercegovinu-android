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

    public interface PlaceCallback{
        void onSuccess(Place place);
        void onError(String message);
    }

    public void getPlaces(int page, int size,String q, Long categoryId, PlacesCallback callback){
        RetrofitClient.getApiService().getPlaces(page, size, q, categoryId)
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

    public void getPlaceById(long id,PlaceCallback callback){
        RetrofitClient.getApiService().getPlaceById(id).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if (response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    // Backend vraca 404 kad mjesto ne postoji (npr obrisano je u medjuvremenu)
                    callback.onError("Mjesto vise ne postoji");
                }else {
                    callback.onError("Greska server (kod " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                callback.onError("Nema veze sa serverom");
            }
        });
    }



}
