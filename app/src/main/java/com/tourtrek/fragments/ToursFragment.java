package com.tourtrek.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tourtrek.R;
import com.tourtrek.viewmodels.ToursViewModel;

public class ToursFragment extends Fragment {

    private ToursViewModel toursViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        toursViewModel = new ViewModelProvider(this).get(ToursViewModel.class);

        View root = inflater.inflate(R.layout.fragment_tours, container, false);

        final TextView textView = root.findViewById(R.id.text_dashboard);

        toursViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}