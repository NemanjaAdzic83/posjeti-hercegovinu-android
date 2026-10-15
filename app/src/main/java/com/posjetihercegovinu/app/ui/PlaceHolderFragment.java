package com.posjetihercegovinu.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.posjetihercegovinu.app.databinding.FragmentPlaceholderBinding;

public class PlaceHolderFragment extends Fragment {

    private static final String ARG_TITLE = "title";

    // Pravi fragment sa naslovom . Fragment se ne pravi sa "new" sa parametrima, nego se podaci
    // stavljaju u argumente koje Android sacuva i pri rotaciji
    public static PlaceHolderFragment newInstance(String title){
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        FragmentPlaceholderBinding binding = FragmentPlaceholderBinding.inflate(inflater,container,false);
        String title = getArguments() != null ? getArguments().getString(ARG_TITLE,"") : "";
        binding.textTitle.setText(title + ": uskoro");
        return binding.getRoot();
    }
}
