package com.posjetihercegovinu.app.model;

import java.math.BigDecimal;

// Java klasa koja odgovara JSON-u koji vraca backend
// Gson povezuje JSON polja i Java polja po imenu, pa moraju biti ista kao u backendu
public class Place {

    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String address;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private String openingHours;
    private BigDecimal price;
    private String imageUrl;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
