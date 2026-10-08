package com.posjetihercegovinu.app.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.posjetihercegovinu.app.databinding.ActivityPlaceDetailBinding;
import com.posjetihercegovinu.app.model.Place;

import java.math.BigDecimal;

public class PlaceDetailActivity extends AppCompatActivity {

    // Kluc pod kojim saljemo ID mjesta iz liste u ovaj ekran
    public static final String EXTRA_PLACE_ID = "place_id";


    private ActivityPlaceDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Uzmi ID koji je poslala lista. -1 je rezervna vrijednost ako ID nije stigao
        long placeId = getIntent().getLongExtra(EXTRA_PLACE_ID, -1);
        if (placeId == -1){
            finish();
            return;
        }

        PlaceDetailViewModel viewModel = new ViewModelProvider(this).get(PlaceDetailViewModel.class);

        viewModel.getPlace().observe(this, place -> {
            if (place != null){
                showPlace(place);
            }
        });

        viewModel.getLoading().observe(this , isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(this, message ->{
            if (message != null){
                binding.scrollContent.setVisibility(View.GONE);
                binding.textMessage.setText(message);
                binding.layoutMessage.setVisibility(View.VISIBLE);
            }else {
                binding.layoutMessage.setVisibility(View.GONE);
            }
        });

        binding.buttonRetry.setOnClickListener( v -> viewModel.loadPlace(placeId));

        viewModel.loadPLaceIfNeeded(placeId);
    }

    // Upisuje podateke mjeata u elemente ekrana
    private void showPlace(Place place) {
        binding.textName.setText(place.getName());
        binding.textCategory.setText(place.getCategoryName());
        binding.textDescription.setText(place.getDescription());
        binding.textAddress.setText(place.getAddress());
        binding.textHours.setText(place.getOpeningHours());

        BigDecimal price = place.getPrice();
        if (price == null){
            binding.textPrice.setText("-");
        }else if (price.compareTo(BigDecimal.ZERO) == 0){
            binding.textPrice.setText("Besplatan ulaz");
        }else {
            binding.textPrice.setText(price.toPlainString() + " KM");
        }
        binding.scrollContent.setVisibility(View.VISIBLE);
    }

    private String orDash(String text){
        return (text == null || text.trim().isEmpty()) ? "-" : text;
    }

}
