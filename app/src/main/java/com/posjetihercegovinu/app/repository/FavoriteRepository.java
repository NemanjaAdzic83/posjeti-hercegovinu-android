package com.posjetihercegovinu.app.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.posjetihercegovinu.app.data.local.AppDatabase;
import com.posjetihercegovinu.app.data.local.FavoriteDao;
import com.posjetihercegovinu.app.data.local.FavoriteEntity;
import com.posjetihercegovinu.app.model.Place;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteRepository {

    private final FavoriteDao dao;

    // Jedna pozadinska nit koja redom izvrsava upise u bazu
    // static - dijele je svi repository objektio, ne pravimo novu nit svaki put
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public FavoriteRepository(Context context){
        // grtinstance vraca jednu zajednicku bazu(singleton)
        this.dao = AppDatabase.getInstance(context).favoriteDao();
    }

    // Svi favorti (najnoviji prvo). Room sam javlja promjene
    public LiveData<List<FavoriteEntity>> getAll(){
        return dao.getAll();
    }


    // 0 nije favorit, 1 jeste. koristi se za srce na detalju
    public LiveData<Integer> countById(long id){
        return dao.countById(id);
    }

    // Dodaje mjesto u favorite ili osvjezava postojecu kopiju
    public void add(Place place){
        // Pretvaranje radimo ovde, na glavnoj niti, a samo upis ide u pozadinu
        FavoriteEntity entity = toEntity(place);
        executor.execute(() -> dao.insert(entity));
    }

    public void remove(long id){
        executor.execute(() -> dao.deleteById(id));
    }

    // Place(server) -> FavoriteEntity(lokalna baza)
    private FavoriteEntity toEntity(Place place) {
        FavoriteEntity e = new FavoriteEntity();
        e.id = place.getId();
        e.name = place.getName() != null ? place.getName() : "";
        e.categoryName = place.getCategoryName();
        e.address = place.getAddress();
        e.description = place.getDescription();
        e.openingHours = place.getOpeningHours();
        e.imageUrl = place.getImageUrl();
        // BigDecimal -> Double, ali samo ako vrijednost postoji
        e.latitude = place.getLatitude() != null ? place.getLatitude().doubleValue() : null;
        e.longitude = place.getLongitude() != null ? place.getLongitude().doubleValue() : null;
        //Cijena kao tekst
        e.price = place.getPrice() != null ? place.getPrice().toPlainString() : null;
        e.addedAt = System.currentTimeMillis();
        return e;
    }


}
