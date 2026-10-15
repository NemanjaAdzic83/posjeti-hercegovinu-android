package com.posjetihercegovinu.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.posjetihercegovinu.app.data.local.TokenStorage;
import com.posjetihercegovinu.app.databinding.ActivityMainBinding;
import com.posjetihercegovinu.app.model.Category;
import com.posjetihercegovinu.app.model.PageResponse;
import com.posjetihercegovinu.app.model.Place;
import com.posjetihercegovinu.app.network.RetrofitClient;
import com.posjetihercegovinu.app.ui.FavoritesActivity;
import com.posjetihercegovinu.app.ui.LoginActivity;
import com.posjetihercegovinu.app.ui.MapActivity;
import com.posjetihercegovinu.app.ui.PlaceAdapter;
import com.posjetihercegovinu.app.ui.PlaceDetailActivity;
import com.posjetihercegovinu.app.ui.PlaceHolderFragment;
import com.posjetihercegovinu.app.ui.PlaceViewModel;
import com.posjetihercegovinu.app.ui.PlacesFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //private static final long SEARCH_DELAY_MS = 400;

    private ActivityMainBinding binding;

    //Handler sluzi da zakazemo kod za kasnije i da zakazano otkazemo
    private final Handler searchHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceSaved){
        super.onCreate(savedInstanceSaved);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setOnItemSelectedListener(item ->{
            int id = item.getItemId();
            if (id == R.id.nav_places){
                showFragment(new PlacesFragment());
                return true;
            }else if (id == R.id.nav_accommodation){
                showFragment(PlaceHolderFragment.newInstance("Smjestaj"));
                return true;
            } else if (id == R.id.nav_food) {
                showFragment(PlaceHolderFragment.newInstance("Hrana"));
                return true;
            } else if (id == R.id.nav_map) {
                startActivity(new Intent(this, MapActivity.class));
                return false;
            } else if (id == R.id.nav_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return false;
            }
            return false;
        });

        binding.bottomNav.setOnItemReselectedListener(item ->{});

        if (savedInstanceSaved == null){
            binding.bottomNav.setSelectedItemId(R.id.nav_places);
        }


    }

    // Mjenja fragment u okviru ekrana novim
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Poziva se svaki put prije prikaza menija. Prikazuje "Prijava" ako nema prijavljenog
    // korisnika , a "Odjava" ako ima
    public boolean onPrepareOptionsMenu(Menu menu){
        TokenStorage storage = new TokenStorage(this);
        boolean loggedIn = storage.isLoggedIn();

        menu.findItem(R.id.action_login).setVisible(!loggedIn);
        MenuItem logout = menu.findItem(R.id.action_logout);
        logout.setVisible(loggedIn);
        if (loggedIn){
            logout.setTitle("Odjava (" + storage.getName() + ")");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    // Poziva se kad korisnik klikn estavku u maniju na traci, MAPA otvara mapu, a FAVORITI otvara ekran sa sacuvanim mjestima
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_login){
            startActivity(new Intent(this,LoginActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            new TokenStorage(this).clear();
            invalidateOptionsMenu();
            Toast.makeText(this,"Odjavljeni ste", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Poziva se kad se vratimo na ovaj ekran (npr. nakon prijave) , trazi da se meni
    // ponovo pripremi , pa se "Prijava" zamjeni sa "Odjava"
    @Override
    protected void onResume(){
        super.onResume();
        invalidateOptionsMenu();
    }


}