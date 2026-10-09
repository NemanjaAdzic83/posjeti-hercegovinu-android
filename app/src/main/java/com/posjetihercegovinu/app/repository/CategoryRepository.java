package com.posjetihercegovinu.app.repository;

import com.posjetihercegovinu.app.model.Category;
import com.posjetihercegovinu.app.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {

    public interface CategoriesCallback{
        void onSuccess(List<Category> categories);
        void onError(String message);
    }

    public
    void getCategories(CategoriesCallback callback){
        RetrofitClient.getApiService().getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                }else {
                    callback.onError("Greska servera (kod " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                callback.onError("Nema veze sa serverom");
            }
        });
    }

}
