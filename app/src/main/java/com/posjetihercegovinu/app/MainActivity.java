package com.posjetihercegovinu.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.posjetihercegovinu.app.databinding.ActivityMainBinding;
import com.posjetihercegovinu.app.model.PageResponse;
import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.network.RetrofitClient;
import com.posjetihercegovinu.app.ui.PlaceAdapter;
import com.posjetihercegovinu.app.ui.PlaceDetailActivity;
import com.posjetihercegovinu.app.ui.PlaceViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // ViewBinding umjesto setContentView(R.layout...).
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Padding za status bar i navigacionu traku, na korijenski element preko bindinga.
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        PlaceAdapter adapter = new PlaceAdapter( place -> {
            Intent intent = new Intent(this, PlaceDetailActivity.class);
            // ID prvo spremimo u obican long.Ako bismo proslijedili diretno Long objekat
            // Java bi izabrala pogresnu verziju putExtra i getLongExtra bi vratio -1
            long id = place.getId();
            intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_ID, id);
            startActivity(intent);
        });
        binding.recyclerPlaces.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPlaces.setAdapter(adapter);

        PlaceViewModel viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        viewModel.getPlaces().observe(this, places -> {
            adapter.setPlaces(places);
            if (places.isEmpty()){
                binding.textMessage.setText("Nema mjesta za prikaz");
                binding.buttonRetry.setVisibility(View.GONE);
                binding.layoutMessage.setVisibility(View.VISIBLE);
            }else {
                binding.layoutMessage.setVisibility(View.GONE);
            }
        });

        viewModel.getLoading().observe(this, isLoading ->
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        viewModel.getError().observe(this, message -> {
            if (message != null){
                binding.textMessage.setText(message);
                binding.buttonRetry.setVisibility(View.VISIBLE);
                binding.layoutMessage.setVisibility(View.VISIBLE);
            }
        });

        binding.buttonRetry.setOnClickListener( v -> viewModel.loadPlaces());


    }
}