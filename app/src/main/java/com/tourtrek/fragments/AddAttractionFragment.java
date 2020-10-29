package com.tourtrek.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.CurrentPersonalToursAdapter;
import com.tourtrek.adapters.FuturePersonalToursAdapter;
import com.tourtrek.adapters.PastPersonalToursAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment corresponds to the user story for creating a custom attraction.
 * It runs when a user selects the 'add attraction' option from within the fragment showing the list of attractions in a selected tour.
 * The fragment will consist of a form with text fields corresponding to Attraction variables to fill in and a button to collect
 * the contents of them and push the information to Firestore.
 */
public class AddAttractionFragment extends Fragment {

    private static final String TAG = "AddAttractionFragment";
    private FirebaseAuth mAuth;
    private String locationHint = "330 N. Orchard St., Madison, WI, USA";
    private String costHint = "0";
    private String nameHint = "WID";
    private String descriptionHint = "Research center";
    private String startHint = "Oct. 10th,2020 9am";
    private String endHint = "Oct. 10th,2020 10am";
    private EditText locationText;
    private EditText costText;
    private EditText nameText;
    private EditText descriptionText;
    private EditText startText;
    private EditText endText;
    private Tour tour;
    private TourViewModel tourViewModel;
    private FragmentManager fragmentManager;
    /**
     * Default for proper back button usage
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // TODO between onViewCreated and onCreateView, set up the backend to your layout

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize tour view model to get the current tour
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);
        fragmentManager = getActivity().getSupportFragmentManager();
        // Grab a reference to the current view
        View addAttractionView = inflater.inflate(R.layout.fragment_create_attraction, container, false);
        // Grab the tour that was selected
        // this.tour = tourViewModel.getSelectedTour(); // TODO refer to the tour in the view model directly until you save to the database
        // create text fields
        locationText = addAttractionView.findViewById(R.id.attraction_location_et);
        locationText.setHint(locationHint);
        costText = addAttractionView.findViewById(R.id.attraction_cost_et);
        costText.setHint(costHint);
        nameText = addAttractionView.findViewById(R.id.attraction_name_et);
        nameText.setHint(nameHint);
        descriptionText = addAttractionView.findViewById(R.id.attraction_description_et);
        descriptionText.setHint(descriptionHint);
        startText = addAttractionView.findViewById(R.id.attraction_time_start_et);
        startText.setHint(startHint);
        endText = addAttractionView.findViewById(R.id.attraction_time_end_et);
        endText.setHint(endHint);
        // create the update button
        Button addAttractionButton = addAttractionView.findViewById(R.id.attraction_add_btn);
        // set up the action to carry out via the update button
        setUpAddAttractionBtn(addAttractionButton);
        return addAttractionView;
    }

    /**
     * Important for titling
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Add Attraction");
    }

    private void setUpAddAttractionBtn(Button addAttractionBtn){
        // add the attraction to the Firestore in the same way that a Hive would be added to an Apiary in Hive Management, but allow Firestore to create the ID
        // a private helper method for adding to the Firestore will probably be handy here
        addAttractionBtn.setOnClickListener(v -> {
            // first get the information from each EditText
            String inputLocation = locationText.getText().toString();
            String inputCost = costText.getText().toString();
            String inputName = nameText.getText().toString();
            String inputDescription = descriptionText.getText().toString();
            String inputStart = startText.getText().toString();
            String inputEnd = endText.getText().toString();
            // build the new attraction from the input information
            Attraction attr = new Attraction();
            // TODO error if the location does not contain the right kind of information
            if (inputName != null && !inputName.equals("") && inputLocation != null && !inputLocation.equals("")){
                attr.setName(inputName);
                attr.setLocation(inputLocation);
                // proceed only if the other text fields have been populated
                if (inputDescription != null && !inputDescription.equals("")){
                    attr.setDescription(inputDescription);
                }
                if (inputCost != null && !inputCost.equals("")){
                    attr.setCost(Integer.parseInt(inputCost));
                }
                // TODO figure out how to process user time and date information into a Date or Timestamp object
                // add the attraction to Firestore
                addToFirestore(attr);
                // go back once the button is pressed
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void addToFirestore(Attraction attraction){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference newAttractionDoc = db.collection("Attractions").document();     // omit the ID so that Firestore generates a unique one
        newAttractionDoc.set(attraction) // write the attraction to the new document
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "Attraction written to firestore");

                    // Tour currentTour = MainActivity.user.getCurrentTour().get().getResult().toObject(Tour.class); // get a Tour copy of the document
                    tourViewModel.getSelectedTour().addAttractionToAttractions(newAttractionDoc); // Add the new attraction to the Tour


                    if (tourViewModel.getSelectedTour().getTourUID() != null) {
                        syncTour();
                    }

                })
                .addOnFailureListener(e -> {

                })

                .addOnSuccessListener(aVoid -> {

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document"));
    }

    /**
     * Retrieve all tours belonging to this user
     *
     */
    private void syncTour() {
        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Setup collection reference
        CollectionReference toursCollection = db.collection("Tours");
        toursCollection.document(tourViewModel.getSelectedTour().getTourUID()).set(tourViewModel.getSelectedTour()).addOnCompleteListener(v ->
        {
            Log.d(TAG, "Tour written");
        });
    }
}