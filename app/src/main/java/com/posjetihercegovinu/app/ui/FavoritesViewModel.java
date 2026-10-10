package com.posjetihercegovinu.app.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.posjetihercegovinu.app.data.local.FavoriteEntity;
import com.posjetihercegovinu.app.repository.FavoriteRepository;

import java.util.List;


public class FavoritesViewModel extends AndroidViewModel {

    private final FavoriteRepository favoriteRepository;
    private final LiveData<List<FavoriteEntity>> favorites;

    // Konstruktor pravi repository i odmah uzima LiveData sa listom favorita
    // Room tu listu sam osvjezava kad god se tabela promjeni
    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = new FavoriteRepository(application);
        favorites = favoriteRepository.getAll();
    }

    // Getter - daje ekranu listu favorita za posmatranje
    public LiveData<List<FavoriteEntity>> getFavorites(){
        return favorites;
    }

    // Uklanja mjesto iz favorita po ID-ju , lista se sama osvjezi jer ekran posmatra LiveData iz baze
    public void removeFavorites(long id){
        favoriteRepository.remove(id);
    }



}
