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
import com.posjetihercegovinu.app.ui.PlaceViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final long SEARCH_DELAY_MS = 400;

    private ActivityMainBinding binding;

    //Handler sluzi da zakazemo kod za kasnije i da zakazano otkazemo
    private final Handler searchHandler = new Handler(Looper.getMainLooper());

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

        viewModel.getCategories().observe(this, categories ->  showCategoriesChips(categories, viewModel));

        viewModel.getLoading().observe(this, isLoading ->
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        viewModel.getError().observe(this, message -> {
            if (message != null){
                adapter.setPlaces(new ArrayList<>());
                binding.textMessage.setText(message);
                binding.buttonRetry.setVisibility(View.VISIBLE);
                binding.layoutMessage.setVisibility(View.VISIBLE);
            }
        });

        binding.buttonRetry.setOnClickListener( v -> viewModel.retry());

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Korsnik protisne trazi na tastaturi, trazi odmah, bez cekanja
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHandler.removeCallbacksAndMessages(null);
                viewModel.setQuery(query);
                binding.searchView.clearFocus(); // zatvara tastaturu
                return true;
            }

            // Poziva se na svako otkucano ili obrisano slovo
            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed( () -> viewModel.setQuery(newText), SEARCH_DELAY_MS);
                return true;
            }
        });
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Poziva se kad korisnik klikn estavku u maniju na traci, MAPA otvara mapu, a FAVORITI otvara ekran sa sacuvanim mjestima
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_map){
            startActivity(new Intent(this, MapActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }else if (item.getItemId() == R.id.action_login){
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            // Brise token i podateke, osvjezava meni i javlja korisniku
            new TokenStorage(this).clear();
            invalidateOptionsMenu();
            Toast.makeText(this, "Odjavljeni ste", Toast.LENGTH_SHORT).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showCategoriesChips(List<Category> categories, PlaceViewModel viewModel) {
        ChipGroup group = binding.chipGroupCategories;

        group.setOnCheckedStateChangeListener(null);
        group.removeAllViews();

        Long selectedId = viewModel.getSelectedCategoryId();

        group.addView(createChip ("Sve", null, selectedId == null));
        for (Category category : categories){
            group.addView(createChip(category.getName(), category.getId(),category.getId().equals(selectedId)));
        }

        group.setOnCheckedStateChangeListener((g,checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            Chip chip = g.findViewById(checkedIds.get(0));
            Long categoryId = (Long) chip.getTag();
            viewModel.selectCategory(categoryId);
        });
    }

    private View createChip(String text, Long categoryId, boolean checked) {
        Chip chip = new Chip(this);
        chip.setId(View.generateViewId());
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        chip.setTag(categoryId);
        chip.setChecked(checked);
        return chip;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        searchHandler.removeCallbacksAndMessages(null);
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

    // Poziva se kad se vratimo na ovaj ekran (npr. nakon prijave) , trazi da se meni
    // ponovo pripremi , pa se "Prijava" zamjeni sa "Odjava"
    @Override
    protected void onResume(){
        super.onResume();
        invalidateOptionsMenu();
    }


}