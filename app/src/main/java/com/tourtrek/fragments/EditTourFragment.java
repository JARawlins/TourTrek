package com.tourtrek.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.CurrentTourAttractionsAdapter;
import com.tourtrek.adapters.EditTourAttractionsAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.Firestore;
import com.tourtrek.utilities.Utilities;
import com.tourtrek.viewModels.TourViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class EditTourFragment extends Fragment {

    private static final String TAG = "EditTourFragment";
    private TourViewModel tourViewModel;
    private Tour tour;
    private EditTourAttractionsAdapter attractionsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView attractionsView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

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

        View editTourView = inflater.inflate(R.layout.fragment_edit_tour, container, false);



        // Initialize view model
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);

        this.tour = tourViewModel.getSelectedTour();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (!tourViewModel.getSelectedTour().getAttractions().isEmpty()) {
            db.collection("Attractions").document(tourViewModel.getSelectedTour().getAttractions().get(tourViewModel.getSelectedTour().getAttractions().size() - 1).getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Attraction attraction = task.getResult().toObject(Attraction.class);

                            ((EditTourAttractionsAdapter) attractionsAdapter).add(attraction);

                        }
                    });
        }


        EditText tourNameEditText = editTourView.findViewById(R.id.edit_tour_tour_name_ct);
        tourNameEditText.setText(tour.getName());
        ImageView tourCoverImageView = editTourView.findViewById(R.id.edit_tour_cover_iv);
        Glide.with(getContext()).load(tour.getCoverImageURI()).into(tourCoverImageView);
        EditText tourLocationEditText = editTourView.findViewById(R.id.edit_tour_tour_location_ct);
        tourLocationEditText.setText(tour.getLocation());
        EditText tourLengthEditText = editTourView.findViewById(R.id.edit_tour_length_ct);
        tourLengthEditText.setText(tour.getLength().toString());
        EditText tourStartEditText = editTourView.findViewById(R.id.edit_tour_startDate_ct);
        Timestamp ts = tour.getStartDate();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String tourStartDate = df.format(ts.toDate());
        tourStartEditText.setText(tourStartDate);
        CheckBox tourIsPublic = editTourView.findViewById(R.id.edit_tour_public_cb);
        tourIsPublic.setChecked(tour.isPubliclyAvailable());
        CheckBox tourNotification = editTourView.findViewById(R.id.edit_tour_notifications_cb);
        tourNotification.setChecked(tour.getNotifications());


        //set onClickListener for image view
        tourCoverImageView.setOnClickListener(v ->{
//            chooseImage();
//            uploadImage();
        });


        // Create a button which directs to addAttractionFragment when pressed
        Button tour_attractions_btn= editTourView.findViewById(R.id.edit_tour_add_attraction_bt);
        // When the button is clicked, switch to the AddAttractionFragment
        tour_attractions_btn.setOnClickListener(v -> {
            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.edit_tour_container, new AddAttractionFragment(), "AddAttractionFragment");
            ft.addToBackStack("AdAttractionFragment").commit();
        });





//        // Create a button which directs to addAttractionFragment when pressed
//        Button tour_attractions_btn = addTourView.findViewById(R.id.tour_attractions_btn);
//
//        // When the button is clicked, switch to the AddAttractionFragment
//        tour_attractions_btn.setOnClickListener(v -> {
//            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//            ft.replace(R.id.nav_host_fragment, new AddAttractionFragment(), "AddAttractionFragment");
//            ft.addToBackStack("AdAttractionFragment").commit();
//        });

        Button editTourSaveButton = editTourView.findViewById(R.id.edit_tour_save_bt);

        editTourSaveButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {


            // Instantiate all fields in add Tour

            final EditText tourNameEditText = editTourView.findViewById(R.id.edit_tour_tour_name_ct);
            final CheckBox publicCheckBox = editTourView.findViewById(R.id.edit_tour_public_cb);
            final CheckBox notificationCheckBox = editTourView.findViewById(R.id.edit_tour_notifications_cb);
            final EditText startDateEditText = editTourView.findViewById(R.id.edit_tour_startDate_ct);
            final EditText locationEditText = editTourView.findViewById(R.id.edit_tour_tour_location_ct);
            final EditText lengthEditText = editTourView.findViewById(R.id.edit_tour_length_ct);
//            final TextView errorTextView = editTourView.findViewById(R.id.edit_tour_error_tv);
//            final ProgressBar loadingProgressBar = addTourView.findViewById(R.id.edit_tour_loading_pb);

            // Close keyboard
            Utilities.hideKeyboard(getActivity());

            // Start loading the progress circle
//            loadingProgressBar.setVisibility(View.VISIBLE);


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
                if (name.equals("")||location.equals("")) {
                     //Show error to user
//                    errorTextView.setVisibility(View.VISIBLE);
                    Log.d(TAG, "Name or location are not both entered");
//                    errorTextView.setText("Name or location are not both entered");
////
//                    // Stop loading progress circle
//                    loadingProgressBar.setVisibility(View.GONE);
                } else {

                    final Tour newTour = new Tour();

                    newTour.setName(name);
                    newTour.setLocation(location);
                    newTour.setLength(length.longValue());
                    newTour.setNotifications(notificationIsOn);
                    newTour.setPubliclyAvailable(isPublic);
                    newTour.setStartDate(startDate);
                    newTour.setAttractions(new ArrayList<DocumentReference>());




                    final FirebaseFirestore db = FirebaseFirestore.getInstance();


                    newTour.setTourUID(tour.getTourUID());


                    db.collection("Tours") .document(newTour.getTourUID()).set(newTour)
                            .addOnSuccessListener(w ->{
                        Log.d(TAG, "Tour written to firestore ");

                        // Update the user
                        Firestore.updateUser();

                        getActivity().getSupportFragmentManager().popBackStack();
                    }).addOnFailureListener(a -> {
                        Log.w(TAG, "Error saving hive to firestore");
                    });


                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    });

        // set up the recycler view of attractions
        configureRecyclerViews(editTourView);
        configureSwipeRefreshLayouts(editTourView);

        return editTourView;
    }

    /**
     * Configure the recycler view
     *
     * @param view current view
     */
    public void configureRecyclerViews(View view) {
        // Get our recycler view from the layout
        attractionsView = view.findViewById(R.id.current_tour_attractions_rv);
        // Improves performance because content does not change size
        attractionsView.setHasFixedSize(true);
        // Only load 10 tours before loading more
        attractionsView.setItemViewCacheSize(10);
        // Enable drawing cache
        attractionsView.setDrawingCacheEnabled(true);
        attractionsView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        // User linear layout manager
        RecyclerView.LayoutManager attractionsLayoutManager = new LinearLayoutManager(getContext());
        attractionsView.setLayoutManager(attractionsLayoutManager);
        // Specify an adapter
        attractionsAdapter = new EditTourAttractionsAdapter(getContext());
//        attractionsAdapter = new EditTourAttractionsAdapter(getContext());
        fetchAttractionsAsync();
        // set the adapter
        attractionsView.setAdapter(attractionsAdapter);
//        // Stop showing progressBar when items are loaded
//        attractionsView
//                .getViewTreeObserver()
//                .addOnGlobalLayoutListener(
//                        ((CurrentTourAttractionsAdapter) attractionsAdapter)::stopLoading);
        }


    /**
     * Retrieve all attractions belonging to this user
     *
     */
    private void fetchAttractionsAsync() {

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference attractionsCollection = db.collection("Attractions");

        // Pull out the UID's of each tour that belongs to this user
        List<String> usersAttractionUIDs = new ArrayList<>();
        if (!tour.getAttractions().isEmpty()) {
            for (DocumentReference documentReference : tour.getAttractions()) {
                usersAttractionUIDs.add(documentReference.getId());
            }
        }

        // Query database
        attractionsCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.w(TAG, "No documents found in the Attractions collection for this user");
                    }
                    else {

                        // Final list of tours for this category
                        List<Attraction> usersAttractions = new ArrayList<>();

                        // Go through each document and compare the dates
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                            // First check that the document belongs to the user
                            if (usersAttractionUIDs.contains(document.getId())) {
                                usersAttractions.add(document.toObject(Attraction.class));
                            }
                        }

                        ((EditTourAttractionsAdapter) attractionsAdapter).clear();
                        ((EditTourAttractionsAdapter) attractionsAdapter).addAll(usersAttractions);
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });
    }

    /**
     * Configure the swipe down to refresh function of our recycler view
     *
     * @param view current view
     */
    public void configureSwipeRefreshLayouts(View view) {

        // Current
        swipeRefreshLayout = view.findViewById(R.id.current_tour_attractions_srl);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchAttractionsAsync());

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Edit Tour");

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

}