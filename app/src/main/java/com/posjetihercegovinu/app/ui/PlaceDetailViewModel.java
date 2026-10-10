package com.posjetihercegovinu.app.ui;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.repository.FavoriteRepository;
import com.posjetihercegovinu.app.repository.PlaceRepository;

import org.jetbrains.annotations.NotNull;

// AndroidViewModel - ViewModel koji ima pristup Application kontekstu
public class PlaceDetailViewModel extends AndroidViewModel {

    private final PlaceRepository repository = new PlaceRepository();
    private final FavoriteRepository favoriteRepository;

    private final MutableLiveData<Place> place = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    // ID mjesta ciji se favorit status prati. Kad  se postavi isFavorite se sam osvjezi
    private final MutableLiveData<Long> placeId = new MutableLiveData<>();
    private final  LiveData<Boolean> isFavorite;

    public PlaceDetailViewModel(@NotNull Application application){
        super(application);
        favoriteRepository = new FavoriteRepository(application);

        //switchMap - kad se placeId promjeni, uzmi novi LiveData iz baze za taj ID
        // map - broj redova (0/1) pretvaramo u true/false
        isFavorite = Transformations.map(
                Transformations.switchMap(placeId, favoriteRepository::countById),
                count -> count != null && count > 0
        );
    }


    public LiveData<Place> getPlace(){
        return place;
    }
    public LiveData<Boolean> getLoading(){
        return loading;
    }
    public LiveData<String> getError(){
        return error;
    }
    public LiveData<Boolean> getIsFavorite(){
        return isFavorite;
    }

    // Poziva se iz Activity-ja cim imamo ID
    public void setPlaceId(long id){
        if (!Long.valueOf(id).equals(placeId.getValue())){
            placeId.setValue(id);
        }
    }

    // Klik na srce, ako je favorit ukloni, inace dodaj
    public void toggleFavorite(){
        Place current = place.getValue();
        if (current == null) return; // jos nema podataka za spremanje
        if (Boolean.TRUE.equals(isFavorite.getValue())){
            favoriteRepository.remove(current.getId());
        }else {
            favoriteRepository.add(current);
        }
    }


    // Ucitaj mjesto ako ga jos nemamo. Nakon rotacije ekrana viewModel vec ima podatak,
    // pa se nepotrebno me salje nov zahtjev
    public void loadPLaceIfNeeded(long id){
        if (place.getValue() == null && !Boolean.TRUE.equals(loading.getValue())){
            loadPlace(id);
        }
    }

    // Uvijek ucitaj ispocetka (koristi ze i za dugme "Pokusaj ponovo")
    public void loadPlace(long id) {
        loading.setValue(true);
        error.setValue(null);
        repository.getPlaceById(id, new PlaceRepository.PlaceCallback() {
            @Override
            public void onSuccess(Place result) {
                loading.setValue(false);
                place.setValue(result);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }


}
