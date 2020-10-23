package com.tourtrek.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;

public class AddAttractionFragment extends Fragment {

    private static final String TAG = "AddAttractionFragment";
    private FirebaseAuth mAuth;
    private String locationHint = "Address in the form: 330 N. Orchard St., Madison, WI";
    private String costHint = "US dollar cost estimate";
    private String nameHint = "Attraction name: e.g. Hershel's donut shop, Road Runner Inn, etc.";
    private String descriptionHint = "Description: e.g. Hershel's donut shop has an awesome array of sweet delights.";

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
        EditText locationText = addAttractionView.findViewById(R.id.attraction_location_et);
        locationText.setHint(locationHint);
        EditText costText = addAttractionView.findViewById(R.id.attraction_cost_et);
        costText.setHint(costHint);
        EditText nameText = addAttractionView.findViewById(R.id.attraction_name_et);
        nameText.setHint(nameHint);
        EditText descriptionText = addAttractionView.findViewById(R.id.attraction_description_et);
        descriptionText.setHint(descriptionHint);
        // create the update button
        Button addAttractionButton = addAttractionView.findViewById(R.id.attraction_add_btn);

        // set up the action to do via the update button
            // add the attraction to the Firestore in the same way that a Hive would be added to an Apiary in Hive Management
                // a private helper method for adding to the Firestore will probably be handy here

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

}