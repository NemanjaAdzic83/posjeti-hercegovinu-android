package com.posjetihercegovinu.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

//@Dao (Data Access Object) - Room zna da ovde nalazi upite prema bazi
@Dao
public interface FavoriteDao {

    // Dodaje favorit. REPLACE-ako mjesto sa tim ID-jem vec postoji
    // stari red se zamjeni novim. Tako se kopija osvjezava bez gresaka
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntity favorite);

    // Brise favorit po ID-ju mjesta
    @Query("DELETE FROM favorites WHERE id = :id")
    void deleteById(long id);

    // Svi favoriti , najnoviji prvo . LiveData- Room sam javlja ekranu kad god se tabela promjeni
    //pa se lista osvjei bez rucnog posla
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    LiveData<List<FavoriteEntity>> getAll();

    // Da li je jesto favorit? Vraca redove (0 ili 1) za srce na detalju
    // I odje LiveData- srcesesamo prebaci cim se favorit doda ili ukloni
    @Query("SELECT COUNT(*) FROM favorites WHERE id = :id")
    LiveData<Integer> countById(long id);



}
