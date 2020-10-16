package com.tourtrek.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.User;
import com.tourtrek.utilities.Utilities;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get instance of Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Setup a callback for the back button being pressed
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Leave empty since we don't want to user to go back to another screen
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // Get the current nav backstack
        NavController navController = NavHostFragment.findNavController(this);

        // Display login screen if no user was previous logged in
        if (mAuth.getCurrentUser() == null || MainActivity.user == null) {
            navController.navigate(R.id.navigation_login);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View profileFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        if (MainActivity.user != null) {

            // TODO: This is where we will load the users information into their profile

            // Set the users username on their profile
            TextView usernameTextView = profileFragmentView.findViewById(R.id.profile_username_tv);
            usernameTextView.setText(MainActivity.user.getUsername());

        }

        // Setup handler for logout button
        setupLogoutButtonHandler(profileFragmentView);

        return profileFragmentView;
    }

    public void setupLogoutButtonHandler(final View view) {

        Button logoutButton = view.findViewById(R.id.profile_logout_btn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set the global user to null
                MainActivity.user = null;

                // Sign the user out of mAuth
                mAuth.signOut();

                // Get the current navController
                NavController navController = NavHostFragment.findNavController(ProfileFragment.this);

                navController.navigate(R.id.navigation_login);
            }
        });
    }

}