package com.tourtrek.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;

/**
 * This fragment corresponds to the user story for creating a custom attraction.
 * It runs when a user selects the 'add attraction' option from within the fragment showing the list of attractions in a selected tour.
 * The fragment will consist of a form with text fields corresponding to Attraction variables to fill in and a button to collect
 * the contents of them and push the information to Firestore.
 */
public class AddAttractionFragment extends Fragment {

    private static final String TAG = "AddAttractionFragment";
    private FirebaseAuth mAuth;
    private String locationHint = "Address in the form: 330 N. Orchard St., Madison, WI, USA";
    private String costHint = "US dollar cost estimate";
    private String nameHint = "Attraction name: e.g. Hershel's donut shop, Road Runner Inn, etc.";
    private String descriptionHint = "Description: e.g. Hershel's donut shop has an awesome array of sweet delights.";
    private EditText locationText;
    private EditText costText;
    private EditText nameText;
    private EditText descriptionText;
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

        View addAttractionView = inflater.inflate(R.layout.add_attraction_fragment, container, false);

        // create text fields
        locationText = addAttractionView.findViewById(R.id.attraction_location_et);
        locationText.setHint(locationHint);
        costText = addAttractionView.findViewById(R.id.attraction_cost_et);
        costText.setHint(costHint);
        nameText = addAttractionView.findViewById(R.id.attraction_name_et);
        nameText.setHint(nameHint);
        descriptionText = addAttractionView.findViewById(R.id.attraction_description_et);
        descriptionText.setHint(descriptionHint);

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
            // build the new attraction from the input information
            Attraction attr = new Attraction();
            // TODO error if the location does not contain the right kind of information
            if (inputName != null && !inputName.equals("")){
                attr.setName(inputName);
                // proceed only if the other text fields have been populated
                if (inputDescription != null && !inputDescription.equals("")){
                    attr.setDescription(inputDescription);
                }
                if (inputCost != null && !inputCost.equals("")){
                    attr.setCost(Integer.parseInt(inputCost));
                }
                if (inputLocation != null && !inputLocation.equals("")){
                    attr.setLocation(inputLocation);
                }
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
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Attraction written to firestore");

                        Tour currentTour = MainActivity.user.getCurrentTour().get().getResult().toObject(Tour.class); // get a Tour copy of the document
                        currentTour.getAttractions().add(newAttractionDoc); // Add the new attraction to the Tour
                        // Update the Firestore document with the new Tour object data
                        MainActivity.user.getCurrentTour().set(currentTour);

                        //finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document");
                    }
                });
    }
}