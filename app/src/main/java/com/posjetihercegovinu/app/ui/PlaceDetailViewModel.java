package com.posjetihercegovinu.app.ui;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.repository.PlaceRepository;

public class PlaceDetailViewModel extends ViewModel {

    private final PlaceRepository repository = new PlaceRepository();

    private final MutableLiveData<Place> place = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();


    public LiveData<Place> getPlace(){
        return place;
    }
    public LiveData<Boolean> getLoading(){
        return loading;
    }
    public LiveData<String> getError(){
        return error;
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
