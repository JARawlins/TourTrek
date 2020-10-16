package com.tourtrek.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.data.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // If the user was previously logged in, load their information here
        if (mAuth.getCurrentUser() != null) {

            // Get instance of firestore
            final FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Get the users info from the firestore
            db.collection("Users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            // Get the result from the db
                            DocumentSnapshot userDocument = task.getResult();

                            // Convert data into user object
                            if (userDocument != null && userDocument.exists()) {
                                MainActivity.user = userDocument.toObject(User.class);
                            }

                            // Set the content view and load
                            setContentView(R.layout.activity_main);

                            BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

                            // Passing each menu ID as a set of Ids because each
                            // menu should be considered as top level destinations.
                            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                                    R.id.navigation_tour_market,
                                    R.id.navigation_tours,
                                    R.id.navigation_profile)
                                    .build();

                            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

                            NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);

                            NavigationUI.setupWithNavController(bottomNavigationView, navController);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Failed to read user from firestore");
                        }
                    });
        }
        else {
            // Set the content view
            setContentView(R.layout.activity_main);

            BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_tour_market,
                    R.id.navigation_tours,
                    R.id.navigation_profile)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle(title);
    }
}