package com.posjetihercegovinu.app.network;

import com.posjetihercegovinu.app.model.AuthResponse;
import com.posjetihercegovinu.app.model.Category;
import com.posjetihercegovinu.app.model.LoginRequest;
import com.posjetihercegovinu.app.model.PageResponse;
import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.model.RegisterRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;

// Retrofit od ovoga interfejsa sam pravi implementaciju, svaka metoda = jedan endpoint
public interface ApiService {

    // GET /api/places?page=0&size=10&size=50&q=most&categoryId=2  (Query parametri se dodaju posle "?")
    // Ako je q ili categoryId null, Retrofit taj parametar uopste ne salje u URL
    @GET("api/places")
    Call<PageResponse<Place>> getPlaces(@Query("page") int page,
                                        @Query("size") int size,
                                        @Query("q") String q,
                                        @Query("categoryId") Long categoryId);

    // GET /api/places/5 -- jedno mjesto
    // @Path zamjenjuje {id} u adresi vrijednoscu parametra (kao @PathVariable na backendu)
    @GET("/api/places/{id}")
    Call<Place> getPlaceById(@Path("id") long id);

    // GET /api/categories - backend vraca obicnu listu(nje stranicu)
    @GET("/api/categories")
    Call<List<Category>> getCategories();

    // @POST("api/auth/login") - salje email i lozinku (kao JSON), vraca token i korisnika
    @POST("api/auth/login")
    Call<AuthResponse> login (@Body LoginRequest request);

    // @POST("api/auth/register") - salje ime, email i lozinku, vraca token i korisnika
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);


}
