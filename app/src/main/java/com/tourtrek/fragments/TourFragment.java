package com.tourtrek.fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.CurrentTourAttractionsAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.Firestore;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TourFragment extends Fragment {

    private static final String TAG = "TourFragment";
    private TourViewModel tourViewModel;
    private Tour tour;
    private RecyclerView attractionsView;
    private RecyclerView.Adapter attractionsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button tour_attractions_btn;
    private EditText tourLocation;
    private EditText tourCost;
    private EditText timeText;
    private EditText tourNameTextView;
    private Button edit_tour_update_btn;
    private Button edit_tour_share_btn;
    private Button edit_tour_picture_btn;

    private Button tour_edit_btn;

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
        // Initialize tourMarketViewModel to get the current tour
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);
        // Grab a reference to the current view
        View tourView = inflater.inflate(R.layout.fragment_edit_tour, container, false);
        // Grab the tour that was selected
        this.tour = tourViewModel.getSelectedTour();
        // tourNameTextView = tourView.findViewById(R.id.tour_tour_name_tv);
        tourNameTextView = tourView.findViewById(R.id.edit_tour_name_et);
        tourNameTextView.setText(tour.getName());

        // Create a button which directs to addAttractionFragment when pressed
        tour_attractions_btn = tourView.findViewById(R.id.edit_tour_add_attraction_btn);
        tour_attractions_btn.setVisibility(View.INVISIBLE);
        tourIsUsers(this.tour);
        // When the button is clicked, switch to the AddAttractionFragment
        tour_attractions_btn.setOnClickListener(v -> {
            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new AddAttractionFragment(), "AddAttractionFragment");
            ft.addToBackStack("AddAttractionFragment").commit();
        });
        // set up fields to be made visible or invisible
        tourNameTextView.setEnabled(false);
        tourLocation = tourView.findViewById(R.id.edit_tour_location_et);
        tourLocation.setEnabled(false);
        tourCost = tourView.findViewById(R.id.edit_tour_cost_et);
        tourCost.setEnabled(false);
        timeText = tourView.findViewById(R.id.edit_tour_time_et);
        timeText.setEnabled(false);
        edit_tour_update_btn = tourView.findViewById(R.id.edit_tour_update_btn);
        edit_tour_update_btn.setVisibility(View.INVISIBLE);
        edit_tour_share_btn = tourView.findViewById(R.id.edit_tour_share_btn);
        edit_tour_share_btn.setVisibility(View.INVISIBLE); // always invisible for now because sharing functionality is not added
        ImageView tourCoverImageView = tourView.findViewById(R.id.edit_tour_cover_iv);
        edit_tour_picture_btn = tourView.findViewById(R.id.edit_tour_picture_btn);
        edit_tour_picture_btn.setVisibility(View.INVISIBLE);
        edit_tour_picture_btn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            int PICK_IMAGE = 1;
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        Glide.with(getContext()).load(tour.getCoverImageURI()).into(tourCoverImageView);

        // set up the recycler view of attractions
        configureRecyclerViews(tourView);
        configureSwipeRefreshLayouts(tourView);
        return tourView;
    }


    private void setUpEditPictureBtn(Button editPictureBtn){

    }

    /**
     * Configure the recycler view
     *
     * @param view current view
     */
    public void configureRecyclerViews(View view) {
        // Get our recycler view from the layout
        attractionsView = view.findViewById(R.id.tour_attractions_rv);
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
        attractionsAdapter = new CurrentTourAttractionsAdapter(getContext());
        fetchAttractionsAsync();
        // set the adapter
        attractionsView.setAdapter(attractionsAdapter);
        // Stop showing progressBar when items are loaded
        attractionsView
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        () -> ((CurrentTourAttractionsAdapter)attractionsAdapter).stopLoading());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(tour.getName());
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

                        ((CurrentTourAttractionsAdapter) attractionsAdapter).clear();
                        ((CurrentTourAttractionsAdapter) attractionsAdapter).addAll(usersAttractions);
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
        swipeRefreshLayout = view.findViewById(R.id.attractions_srl);
        swipeRefreshLayout.setOnRefreshListener(() -> fetchAttractionsAsync());

    }

    /**
     * If the tour belongs to the user, the button to add an attraction is made visible.
     * @param currentTour
     */
    public void tourIsUsers(Tour currentTour){
        // query the Tours collection reference
        // process the result and confirm that there is a tour with the ID of the current tour in the user's list of tours

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Setup collection reference
        CollectionReference toursCollection = db.collection("Tours");
        // Pull out the UID's of each tour that belongs to this user
        List<String> usersToursUIDs = new ArrayList<>();
        if (!MainActivity.user.getTours().isEmpty()) {
            for (DocumentReference documentReference : MainActivity.user.getTours()) {
                usersToursUIDs.add(documentReference.getId());
            }
        }
        // Query database
        toursCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.w(TAG, "No documents found in the Tours collection");
                    }
                    else {
                        System.out.println(usersToursUIDs);
                        // First check that the document belongs to the user
                        if (usersToursUIDs.contains(this.tour.getTourUID())) {
                            this.tour_attractions_btn.setVisibility(View.VISIBLE);
                            tourNameTextView.setEnabled(true);
                            tourLocation.setEnabled(true);
                            tourCost.setEnabled(true);
                            timeText.setEnabled(true);
                            edit_tour_update_btn.setVisibility(View.VISIBLE);
                            edit_tour_picture_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(resultCode == Activity.RESULT_OK) {
            assert imageReturnedIntent != null;
            uploadImageToDatabase(imageReturnedIntent);
        }
    }

    /**
     * Uploads an image to the Profile Images cloud storage.
     *
     * @param imageReturnedIntent intent of the image being saved
     */
    public void uploadImageToDatabase(Intent imageReturnedIntent) {

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        // Uri to the image
        Uri selectedImage = imageReturnedIntent.getData();

        final UUID imageUUID = UUID.randomUUID();

        final StorageReference storageReference = storage.getReference().child("TourCoverPictures/" + imageUUID);

        final UploadTask uploadTask = storageReference.putFile(selectedImage);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception -> Log.e(TAG, "Error adding image: " + imageUUID + " to cloud storage"))
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i(TAG, "Successfully added image: " + imageUUID + " to cloud storage");

                    storage.getReference().child("TourCoverPictures/" + imageUUID).getDownloadUrl()
                            .addOnSuccessListener(uri -> {

                                tour.setCoverImageURI(uri.toString());

                                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("Tours") .document(tour.getTourUID()).set(tour)
                                        .addOnSuccessListener(w ->{
                                            Log.d(TAG, "Tour written to firestore ");

                                            // Update the user
                                            Firestore.updateUser();

                                            //getActivity().getSupportFragmentManager().popBackStack();
                                        }).addOnFailureListener(a -> {
                                    Log.w(TAG, "Error saving hive to firestore");
                                });


                                //getActivity().getSupportFragmentManager().popBackStack();

                            })
                            .addOnFailureListener(exception -> {
                                Log.e(TAG, "Error retrieving uri for image: " + imageUUID + " in cloud storage, " + exception.getMessage());
                            });
                });
    }
}

