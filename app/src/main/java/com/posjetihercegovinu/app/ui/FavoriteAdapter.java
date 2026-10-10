package com.posjetihercegovinu.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.posjetihercegovinu.app.data.local.FavoriteEntity;
import com.posjetihercegovinu.app.databinding.ItemPlaceBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter  extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    // interfejs preko koga adapter javlja Activity-ju da je neko kliknuo na favorit
    public interface OnFavoriteClickListener{
        void onFavoriteClick(FavoriteEntity favorite);
    }

    private final List<FavoriteEntity> favorites = new ArrayList<>();
    private final OnFavoriteClickListener listener;


    //  kontruktor pamti ko treba da bude obavjesten o kliku
    public FavoriteAdapter(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    public void setFavorites(List<FavoriteEntity> newFavorites){
        favorites.clear();
        if (newFavorites != null){
            favorites.addAll(newFavorites);
        }
        notifyDataSetChanged();
    }

    // Pravi novu kartice za item_place.xml
    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaceBinding binding = ItemPlaceBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new FavoriteViewHolder(binding);
    }

    // Upisuje podatke favorita u karticu na datoj poziciji i postavlja klik na karticu
    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        FavoriteEntity favorite = favorites.get(position);
        holder.binding.textName.setText(favorite.name);
        holder.binding.textCategory.setText(favorite.categoryName != null ? favorite.categoryName : "");
        holder.binding.textAddress.setText(favorite.address != null ? favorite.address : "");
        holder.binding.getRoot().setOnClickListener( v -> listener.onFavoriteClick(favorite));
    }

    // Vraca koliko ima favorita u list
    @Override
    public int getItemCount() {
        return favorites.size();
    }

    // drzi reference na elemente jedne kartice(ViewBinding), da ih ne traimo pri svakom skrolu
    public class FavoriteViewHolder extends RecyclerView.ViewHolder{
        final ItemPlaceBinding binding;
        // KOnstruktor prosledjuje korijenski element kartice roditeljskoj klasi i pamti binding
        public FavoriteViewHolder(@NonNull ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
