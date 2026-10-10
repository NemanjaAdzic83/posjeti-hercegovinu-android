package com.posjetihercegovinu.app.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "favorites")
public class FavoriteEntity {

    // Primarni kljuc je ID mjesta sa servera. Tako isto mjesto ne mozemo spremiti dva puta
    // a spremanje ponovo samo osvjezi staru kopiju
    @PrimaryKey
    public long id;

    // @Notnull - naziv je obavezan (Room tada pravi kolonu NOT NULL)
    @NotNull
    public String name;

    public String categoryName;
    public String address;
    public String description;
    public String openingHours;
    public String imageUrl;

    //Koordinate cuvamo kao double. Room ne zan sam spremiti BigDecimal,
    // a za mapu nam double i treba
    public Double latitude;
    public Double longitude;

    // Cijena kao tekst(npr. "15.00"), da ne gubimo tecnost i izbjegavamo konverter
    public String price;

    // kad je mjesto dodano u favorite(milisekunde), sluzi za sortiranje: zadnje dodano prvo
    public long addedAt;

}
