package com.posjetihercegovinu.app.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.posjetihercegovinu.app.model.Category;
import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.repository.CategoryRepository;
import com.posjetihercegovinu.app.repository.PlaceRepository;

import java.util.List;
import java.util.Objects;

/*
    ViewModel drzi podatke ekrana. Ostaje ziv i kad se telefon rotira
    Activity se tada unisti i napravi ponovo , ali ViewMOdel NE
 */
public class PlaceViewModel extends ViewModel {

    private final PlaceRepository placeRepository = new PlaceRepository();
    private final CategoryRepository categoryRepository = new CategoryRepository();

    // MutableLiveData - podatak koji ViewModel moze mjenjati
    // Ekranu ga izlazemo kao LiveData(asmo citanje) , da ga ekran slucajno ne promjeni
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    // trenutni izbor korisnika. "" - bez pretrage, null - sve kategorije
    private String currentQuery = "";
    private Long selectedCategoryId = null;

    // brojac zahtjeva
    private int latestRequestId = 0;

    public LiveData<List<Place>> getPlaces(){
        return places;
    }
    public LiveData<List<Category>> getCategories(){
        return categories;
    }
    public LiveData<Boolean> getLoading(){
        return loading;
    }
    public LiveData<String> getError(){
        return error;
    }

    public Long getSelectedCategoryId(){
        return selectedCategoryId;
    }

    // Cim se ViewMOdel napravi , odmah ucitaj mjesta
    public PlaceViewModel(){
        loadCategories();
        loadPlaces();

    }

    // Poziva se kad korisnik mjenja tekst pretrage
    public void setQuery(String query){
        String clean = (query == null) ? "" : query.trim();
        if (clean.equals(currentQuery)){
            return;
        }
        currentQuery = clean;
        loadPlaces();
    }

    // Poziva se kad korisnik klikne cip, null = sve
    public void selectCategory(Long categoryId){
        if (Objects.equals(categoryId,selectedCategoryId)){
            return;
        }
        selectedCategoryId = categoryId;
        loadPlaces();
    }

    // Dugme "pokusaj ponovo" - ponovo ucitaj mjesta , a kategorije samo ako ih imamo
    public void retry(){
        loadPlaces();
        if (categories.getValue() == null){
            loadCategories();
        }
    }

    private void loadCategories() {
        categoryRepository.getCategories(new CategoryRepository.CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> result) {
                categories.setValue(result);
            }

            @Override
            public void onError(String message) {
                // Bez kategorija lista i dalje radi, saom nema cipova. Zato ne prikazuje gresku
            }
        });
    }


    // Javna metoda da je dugme "Pokusaj ponovo" moze pozvati
    public void loadPlaces(){
        final  int requestId = ++latestRequestId;
        loading.setValue(true);  // pokazi kruzic
        error.setValue(null);    // obrisi staru gresku

        // Prazan tekst saljemo kao null da parametar "q" ne ide u URL
        String q = currentQuery.isEmpty() ? null : currentQuery;

        placeRepository.getPlaces(0, 50, q, selectedCategoryId, new PlaceRepository.PlacesCallback() {
            @Override
            public void onSuccess(List<Place> result) {
                if (requestId != latestRequestId) return;
                loading.setValue(false);
                places.setValue(result);
            }

            @Override
            public void onError(String message) {
                if (requestId != latestRequestId) return;
                loading.setValue(false);
                error.setValue(message);
            }
        });
    }

}
