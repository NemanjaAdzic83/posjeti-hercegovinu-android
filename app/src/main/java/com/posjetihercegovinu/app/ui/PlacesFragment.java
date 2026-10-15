package com.posjetihercegovinu.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.posjetihercegovinu.app.databinding.FragmentPlacesBinding;
import com.posjetihercegovinu.app.model.Category;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    // Koliko se ceka nakon zadnjeg otkucanog slova prije slanja zahtjeva (debounce)
    private static final long SEARCH_DELAY_MS = 400;

    private FragmentPlacesBinding binding;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());

    // Pravi izgled fragmenta iz fragment_places.xml
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        binding = FragmentPlacesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    // Poziva se cim izgled postoji , postavlja listu , posmatra podatke iz ViewModela
    // (mjesta, kategorije, ucitavanje, greska) i povezuje pretragu i dugme "Pokusaj ponovo"
    @Override
    public void onViewCreated(@NotNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        PlaceAdapter adapter = new PlaceAdapter(place -> {
            Intent intent = new Intent(requireContext(), PlaceDetailActivity.class);
            long id = place.getId();
            intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_ID, id);
            startActivity(intent);
        });

        binding.recyclerPlaces.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerPlaces.setAdapter(adapter);

        PlaceViewModel viewModel = new ViewModelProvider(requireActivity()).get(PlaceViewModel.class);

        viewModel.getPlaces().observe(getViewLifecycleOwner(), places -> {
            adapter.setPlaces(places);
            if (places.isEmpty()){
                binding.textMessage.setText("Nema mjesta za prikaz");
                binding.buttonRetry.setVisibility(View.GONE);
                binding.layoutMessage.setVisibility(View.VISIBLE);
            }else {
                binding.layoutMessage.setVisibility(View.GONE);
            }
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            showCategoriesChips(categories,viewModel);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null){
                adapter.setPlaces(new ArrayList<>());
                binding.textMessage.setText(message);
                binding.buttonRetry.setVisibility(View.VISIBLE);
                binding.layoutMessage.setVisibility(View.VISIBLE);
            }
        });

        binding.buttonRetry.setOnClickListener( v -> viewModel.retry());

        // Vracamo tekst pretrage koji je viewmodel zapamtio
        binding.searchView.setQuery(viewModel.getCurrentQuery(), false);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Korisnik pritisne trazi - trazi se odmah i zatvara tastatura
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHandler.removeCallbacksAndMessages(null);
                viewModel.setQuery(query);
                binding.searchView.clearFocus();
                return true;
            }

            // Poziva se na svako slovo- otkazuje staro trazenje i zakazuje novo
            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed(() -> viewModel.setQuery(newText),SEARCH_DELAY_MS);
                return true;
            }
        });

    }

    private void showCategoriesChips(List<Category> categories, PlaceViewModel viewModel) {
        ChipGroup group = binding.chipGroupCategories;

        group.setOnCheckedStateChangeListener(null);
        group.removeAllViews();

        Long selectedId = viewModel.getSelectedCategoryId();

        group.addView(createChip("Sve", null, selectedId == null));
        for (Category category : categories){
            group.addView(createChip(category.getName(), category.getId(),
                    category.getId().equals(selectedId)));
        }

        group.setOnCheckedStateChangeListener((g, checkedIds) ->{
            if (checkedIds.isEmpty()) return;
            Chip chip = g.findViewById(checkedIds.get(0));
            Long categoryId = (Long) chip.getTag();
            viewModel.selectCategory(categoryId);
        });
    }

    // Pravi cip sa tekstom, u tag pamti id kategorije
    private View createChip(String text, Long categoryId, boolean checked) {
        Chip chip = new Chip(requireContext());
        chip.setId(View.generateViewId());
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        chip.setTag(categoryId);
        chip.setChecked(checked);
        return chip;
    }

    // Poziva se kad se izgled fragmenta unisti(npr promjena taba) - otkazujemo zadanu pretragu
    // i pustamo binding, da se ne drzi stari izgled u memorij
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        searchHandler.removeCallbacksAndMessages(null);
        binding = null;
    }


}
