package com.tourtrek.fragments;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Attraction;
import com.tourtrek.viewModels.TourViewModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This fragment corresponds to the user story for creating a custom attraction.
 * It runs when a user selects the 'add attraction' option from within the fragment showing the list of attractions in a selected tour.
 * The fragment will consist of a form with text fields corresponding to Attraction variables to fill in and a button to collect
 * the contents of them and push the information to Firestore.
 */
public class AddAttractionFragment extends Fragment {

    private static final String TAG = "AddAttractionFragment";
    private EditText locationText;
    private EditText costText;
    private EditText nameText;
    private EditText descriptionText;
    private EditText startText;
    private EditText endText;
    private TourViewModel tourViewModel;
    private TextView errorText;

    /**
     * Default for proper back button usage
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Grab a reference to the current view
        View addAttractionView = inflater.inflate(R.layout.fragment_add_attraction, container, false);

        // Initialize tour view model to get the current tour
        tourViewModel = new ViewModelProvider(getActivity()).get(TourViewModel.class);

        // set up the action to carry out via the update button
        setUpAddAttractionButton(addAttractionView);

        return addAttractionView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Add Attraction");
    }

    private void setUpAddAttractionButton(View view){

        Button addAttractionButton = view.findViewById(R.id.add_attraction_add_btn);

        addAttractionButton.setOnClickListener(v -> {

            locationText = view.findViewById(R.id.add_attraction_location_et);
            costText = view.findViewById(R.id.add_attraction_cost_et);
            nameText = view.findViewById(R.id.add_attraction_name_et);
            descriptionText = view.findViewById(R.id.add_attraction_description_et);
            startText = view.findViewById(R.id.add_attraction_time_start_et);
            endText = view.findViewById(R.id.add_attraction_time_end_et);
            errorText = view.findViewById(R.id.add_attraction_error_tv); // TODO: REMOVE ITERATION 2

            // first get the information from each EditText
            String inputLocation = locationText.getText().toString();
            String inputCost = costText.getText().toString();
            String inputName = nameText.getText().toString();
            String inputDescription = descriptionText.getText().toString();
            String inputStart = startText.getText().toString();
            String inputEnd = endText.getText().toString();

            if (inputLocation.equals("") || inputCost.equals("") || inputName.equals("") ||
                    inputDescription.equals("") || inputStart.equals("") || inputEnd.equals("")) {
                // TODO: ENABLE IN ITERATION 2
                //Toast.makeText(getContext(), "Not all field entered", Toast.LENGTH_SHORT).show();
            }

            // build the new attraction from the input information
            Attraction newAttraction = new Attraction();

            // parse date to firebase format
            try {
                parseDates(inputStart, inputEnd, newAttraction);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // make the error text visible when the user does not provide appropriate inputs
            if (inputName != null && !inputName.equals("") && inputLocation != null && !inputLocation.equals("") &&
                    inputStart != null && !inputStart.equals("") && inputEnd != null && !inputEnd.equals("") &&
                    inputDescription != null && !inputDescription.equals("") && inputCost != null && !inputCost.equals("")){

                newAttraction.setName(inputName);
                newAttraction.setLocation(inputLocation);
                newAttraction.setDescription(inputDescription);
                newAttraction.setCost(Integer.parseInt(inputCost));

                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                final DocumentReference newAttractionDocument = db.collection("Attractions").document();

                newAttraction.setAttractionUID(newAttractionDocument.getId());

                newAttractionDocument.set(newAttraction) // write the attraction to the new document
                        .addOnCompleteListener(task -> {
                            Log.d(TAG, "Attraction written to firestore");

                            tourViewModel.getSelectedTour().addAttraction(newAttractionDocument);

                            // Only update the tour if it has been created in the firestore
                            if (tourViewModel.getSelectedTour().getTourUID() != null) {
                                syncTour();
                            }

                            // go back once the button is pressed
                            getParentFragmentManager().popBackStack();

                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document"));

            }
            else {
                errorText.setText("Enter at least name, location, and start and end time information in the indicated formats");
                errorText.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Update the selected tour
     *
     * This method assumes a tour is already created and has a properly filled UID field
     * https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
     */
    private void syncTour() {
        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference toursCollection = db.collection("Tours");

        toursCollection.document(tourViewModel.getSelectedTour().getTourUID()).set(tourViewModel.getSelectedTour()).addOnCompleteListener(v ->
        {
            Log.d(TAG, "Tour successfully written to firestore");
        });
    }

    /**
     * Parse the user's date input
     *
     * @param startDateStr
     * @param endDateStr
     * @param attraction
     *
     * @throws ParseException
     */
    private void parseDates(String startDateStr, String endDateStr, Attraction attraction) throws ParseException {

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");

        final Timestamp startDate = new Timestamp((Date) formatter.parse(startDateStr));

        final Timestamp endDate = new Timestamp((Date) formatter.parse(endDateStr));

        attraction.setStartDate(startDate);

        attraction.setEndDate(endDate);
    }


}
