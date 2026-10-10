package com.posjetihercegovinu.app.data.local;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


// entities - spisak tabela, version - verzija seme(povecavamo je kad mjenjamo tabelu)
// exportSchema = false - ne izvozimo JSON seme
@Database(entities = {FavoriteEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Room sam napise implementaciju ove metode
    public abstract FavoriteDao favoriteDao();

    // volatile- promjena je odmah vidljiva svim nitima
    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context){
        if (instance == null){
            // synchronized - dvije niti istovremeno ne mogu napraviti dvije baze
            synchronized (AppDatabase.class){
                if (instance == null){
                    instance = Room.databaseBuilder(
                            // getApplicationContext - baza zivi koliko i aplikacija
                            // a ne koliko jedan ekran(izbjegava se curenje memorije)
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "posjeti_hercegovinu.db")
                            .build();
                }
            }
        }
        return instance;
    }

}
