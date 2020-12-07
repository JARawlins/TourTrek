package com.tourtrek.activities;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.data.User;
import com.tourtrek.utilities.PlacesLocal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    public static boolean loading;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Places API
        PlacesLocal.initialize(this);

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

    public void showTimePickerDialog(Button button) {

        final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                int hour = hourOfDay % 12;

                button.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                        minute, hourOfDay < 12 ? "am" : "pm"));

                button.setBackgroundColor(Color.parseColor("#10000000"));

            }
        };

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, minute, DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    /**
     * Disable any tabs from being clicked
     */
    public void disableTabs() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        for (int i = 0 ; bottomNavigationView != null && i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(false);
        }
    }

    /**
     * Enable all tabs from being clicked
     */
    public void enableTabs() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        for (int i = 0 ; bottomNavigationView != null && i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setEnabled(true);
        }
    }

}