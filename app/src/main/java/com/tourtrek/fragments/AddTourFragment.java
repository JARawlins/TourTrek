package com.tourtrek.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.EditTourAttractionsAdapter;
import com.tourtrek.adapters.TourMarketAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.Firestore;
import com.tourtrek.utilities.ItemClickSupport;
import com.tourtrek.utilities.Utilities;
import com.tourtrek.viewModels.TourViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddTourFragment extends Fragment {

    private static final String TAG = "AddTourFragment";
    private TourViewModel tourViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter editTourAttractionsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Pop the last fragment off the stack
                getActivity().getSupportFragmentManager().popBackStack();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View addTourView = inflater.inflate(R.layout.fragment_edit_tour, container, false);

        // Initialize view model
        tourViewModel = new ViewModelProvider(getActivity()).get(TourViewModel.class);

        tourViewModel.setSelectedTour(new Tour());

        tourViewModel.getSelectedTour().setName("Test");

        // Create a button which directs to addAttractionFragment when pressed
        Button tour_attractions_btn= addTourView.findViewById(R.id.edit_tour_add_attraction_bt);
        // When the button is clicked, switch to the AddAttractionFragment
        tour_attractions_btn.setOnClickListener(v -> {
            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.edit_tour_container, new AddAttractionFragment(), "AddAttractionFragment");
            ft.addToBackStack("AddAttractionFragment").commit();
        });



        Button editTourSaveButton = addTourView.findViewById(R.id.edit_tour_save_bt);

        editTourSaveButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {


            // Instantiate all fields in add Tour

            final EditText tourNameEditText = addTourView.findViewById(R.id.edit_tour_tour_name_ct);
            final CheckBox publicCheckBox = addTourView.findViewById(R.id.edit_tour_public_cb);
            final CheckBox notificationCheckBox = addTourView.findViewById(R.id.edit_tour_notifications_cb);
            final EditText startDateEditText = addTourView.findViewById(R.id.edit_tour_startDate_ct);
            final EditText locationEditText = addTourView.findViewById(R.id.edit_tour_tour_location_ct);
            final EditText lengthEditText = addTourView.findViewById(R.id.edit_tour_length_ct);
//            final TextView errorTextView = addTourView.findViewById(R.id.edit_tour_error_tv);
//            final ProgressBar loadingProgressBar = addTourView.findViewById(R.id.edit_tour_loading_pb);

            // Close keyboard
            Utilities.hideKeyboard(getActivity());

            // Start loading the progress circle
//            loadingProgressBar.setVisibility(View.VISIBLE);


            if (tourNameEditText.getText().toString().equals("") || locationEditText.getText().toString().equals("")
            ||lengthEditText.getText().toString().equals("")||startDateEditText.getText().toString().equals("")
            ) {
                Toast.makeText(getContext(),"Not All Fields entered",Toast.LENGTH_SHORT).show();
            }
            else {
                final String name = tourNameEditText.getText().toString();
                final String location = locationEditText.getText().toString();
                final boolean isPublic = publicCheckBox.isChecked();
                final boolean notificationIsOn = notificationCheckBox.isChecked();
                final Integer length = Integer.parseInt(lengthEditText.getText().toString());
                String str_date = startDateEditText.getText().toString();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date date = (Date) formatter.parse(str_date);
                    final Timestamp startDate = new Timestamp(date);
                    // Check to make sure some input was entered
                    if (name.equals("")) {
//                    // Show error to user
//                    errorTextView.setVisibility(View.VISIBLE);
//                    errorTextView.setText("Not all fields entered");
//
//                    // Stop loading progress circle
//                    loadingProgressBar.setVisibility(View.GONE);
                    } else {

                        tourViewModel.getSelectedTour().setName(name);
                        tourViewModel.getSelectedTour().setLocation(location);
                        tourViewModel.getSelectedTour().setLength(length.longValue());
                        tourViewModel.getSelectedTour().setNotifications(notificationIsOn);
                        tourViewModel.getSelectedTour().setPubliclyAvailable(isPublic);
                        tourViewModel.getSelectedTour().setStartDate(startDate);

                        final FirebaseFirestore db = FirebaseFirestore.getInstance();

                        DocumentReference tourRef = db.collection("Tours").document();
                        tourViewModel.getSelectedTour().setTourUID(tourRef.getId());

                        db.collection("Tours").document(tourViewModel.getSelectedTour().getTourUID()).
                                set(tourViewModel.getSelectedTour()).addOnSuccessListener(w -> {
                            Log.d(TAG, "Tour written to firestore ");

                            // Add the tour reference to the user
                            MainActivity.user.addTourToTours(tourRef);

                            // Update the user
                            Firestore.updateUser();

                            getActivity().getSupportFragmentManager().popBackStack();
                        }).addOnFailureListener(a -> {
                            Log.w(TAG, "Error saving hive to firestore");
                        });


//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.d(TAG, "Tour written to firestore ");
//
//                                    // Add the tour reference to the user
//                                    MainActivity.user.addTourToTours(documentReference);
//
//                                    // Update the user
//                                    Firestore.updateUser();
//
//                                    getActivity().getSupportFragmentManager().popBackStack();
//
//                                    // Stop loading progress circle
////                                    loadingProgressBar.setVisibility(View.GONE);
//
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error saving hive to firestore");
//                                }
//                            });

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    });
        System.out.println(MainActivity.user.getTours());

        return addTourView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Add a Tour");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Attractions").document(tourViewModel.getSelectedTour().getAttractions().get(tourViewModel.getSelectedTour().getAttractions().size() - 1).getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Attraction attraction = task.getResult().toObject(Attraction.class);

                        ((EditTourAttractionsAdapter) editTourAttractionsAdapter).add(attraction);

                    }
                });
    }
}