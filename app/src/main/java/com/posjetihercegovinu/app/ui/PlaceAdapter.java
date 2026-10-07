package com.posjetihercegovinu.app.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.posjetihercegovinu.app.databinding.ItemPlaceBinding;
import com.posjetihercegovinu.app.model.Place;


import java.util.ArrayList;
import java.util.List;

/*
    Adapter je most izmedju liste podataka i RecyclerView
    Adapter zna koliko ima stavki , kako napraviti jednu karticu i kako je popuniti podacima
    <PlaceAdapter.PlaceViewHolder> govori koji tim drzaca kartice koristi
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    // Lista mjesta koja se trenutno prikazuje. Na pocetku je prazna
    private final List<Place> places = new ArrayList<>();

    //Zamjeni staru listu novom i reci RecyclerView-u da se ponovo iscrta
    public void setPlaces(List<Place> newPlaces){
        places.clear();
        places.addAll(newPlaces);
        notifyDataSetChanged();
    }

    // Poziva se kad recyclerView treba novu karticu
    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaceBinding binding = ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false);
        return new PlaceViewHolder(binding);
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_place, parent, false);
//        return new PlaceViewHolder(view);
    }

    // Poziva se da kartici na poziciji "position" upise podatke tog mjesta
    // RecyclerView ponovo koristi iste kartice dok skrolujes, pa se ovo pozia cesto
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = places.get(position);
        holder.binding.textName.setText(place.getName());
        // Kategorija ili adresa mogu biti prazne , pa pazimo da ne pise "null"
        holder.binding.textCategory.setText(place.getCategoryName() != null ? place.getCategoryName() : "");
        holder.binding.textAddress.setText(place.getAddress() != null ? place.getAddress() : "");
    }

    // Koliko ukupno ima stavki
    @Override
    public int getItemCount() {
        return places.size();
    }

    // ViewHolder fdrzi reference na elemente jedne kartice, da ih ne trazimo (findById)
    // iznova pri svakom skrolu
    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        final ItemPlaceBinding binding;
        public PlaceViewHolder(@NonNull ItemPlaceBinding binding) {
          super(binding.getRoot());
          this.binding = binding;
        }
    }
}
