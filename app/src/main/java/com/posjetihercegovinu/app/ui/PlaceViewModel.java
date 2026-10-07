package com.posjetihercegovinu.app.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.repository.PlaceRepository;

import java.util.List;

/*
    ViewModel drzi podatke ekrana. Ostaje ziv i kad se telefon rotira
    Activity se tada unisti i napravi ponovo , ali ViewMOdel NE
 */
public class PlaceViewModel extends ViewModel {

    private final PlaceRepository repository = new PlaceRepository();

    // MutableLiveData - podatak koji ViewModel moze mjenjati
    // Ekranu ga izlazemo kao LiveData(asmo citanje) , da ga ekran slucajno ne promjeni
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<Place>> getPlaces(){
        return places;
    }
    public LiveData<Boolean> getLoading(){
        return loading;
    }
    public LiveData<String> getError(){
        return error;
    }

    // Cim se ViewMOdel napravi , odmah ucitaj mjesta
    public PlaceViewModel(){
        loadPlaces();
    }


    // Javna metoda da je dugme "Pokusaj ponovo" moze pozvati
    public void loadPlaces(){
        loading.setValue(true);  // pokazi kruzic
        error.setValue(null);    // obrisi staru gresku
        repository.getPlaces(0, 50, new PlaceRepository.PlacesCallback() {
            @Override
            public void onSuccess(List<Place> result) {
                loading.setValue(false);
                places.setValue(result);
            }

            @Override
            public void onError(String message) {
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

}
