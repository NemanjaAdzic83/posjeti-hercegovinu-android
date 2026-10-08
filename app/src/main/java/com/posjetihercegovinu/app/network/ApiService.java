package com.posjetihercegovinu.app.network;

import com.posjetihercegovinu.app.model.PageResponse;
import com.posjetihercegovinu.app.model.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

// Retrofit od ovoga interfejsa sam pravi implementaciju, svaka metoda = jedan endpoint
public interface ApiService {

    // GET /api/places?page=0&size=10  (Query parametri se dodaju posle "?")
    @GET("api/places")
    Call<PageResponse<Place>> getPlaces(@Query("page") int page,@Query("size") int size);

    // GET /api/places/5 -- jedno mjesto
    // @Path zamjenjuje {id} u adresi vrijednoscu parametra (kao @PathVariable na backendu)
    @GET("/api/places/{id}")
    Call<Place> getPlaceById(@Path("id") long id);

}
