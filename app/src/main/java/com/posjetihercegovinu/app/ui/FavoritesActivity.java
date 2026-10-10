package com.posjetihercegovinu.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.posjetihercegovinu.app.databinding.ActivityFavoritesBinding;

public class FavoritesActivity extends AppCompatActivity {

    private ActivityFavoritesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FavoriteAdapter adapter = new FavoriteAdapter(favorite -> {
            Intent intent = new Intent(this, PlaceDetailActivity.class);
            long id = favorite.id;
            intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_ID,id);
            startActivity(intent);
        });

        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFavorites.setAdapter(adapter);

        FavoritesViewModel viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        viewModel.getFavorites().observe(this, favorites -> {
            adapter.setFavorites(favorites);
            boolean empty = favorites == null || favorites.isEmpty();
            binding.textEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        });

    }



}
