package com.tourtrek.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;

public class ToursFragment extends Fragment {

    private static final String TAG = "ToursFragment";
    private FirebaseAuth mAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Get the current nav backstack
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry navBackStackEntry = navController.getCurrentBackStackEntry();

        // Display login screen if no user was previous logged in
        if (mAuth.getCurrentUser() == null || MainActivity.user == null) {
            navController.navigate(R.id.navigation_login);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View toursFragmentView = inflater.inflate(R.layout.fragment_tours, container, false);

        if (MainActivity.user != null) {
            // TODO: This is where we will load the users past, present and future tours
        }

        return toursFragmentView;
    }
}