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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TourFragment extends Fragment {

    private static final String TAG = "TourFragment";
    private TourViewModel tourViewModel;
    private Tour tour;
    private RecyclerView.Adapter attractionsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button tourAttractionsButton;
    private EditText tourLocationEditText;
    private EditText tourCostEditText;
    private EditText tourLengthEditText;
    private EditText tourNameTextView;
    private Button tourUpdateButton;
    private Button tourCoverButton;
    Button tourShareButton;
    private ImageView coverImageView;

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

        // Grab a reference to the current view
        View tourView = inflater.inflate(R.layout.fragment_edit_tour, container, false);

        // Initialize tourMarketViewModel to get the current tour
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);

        // Grab the tour that was selected
        this.tour = tourViewModel.getSelectedTour();

        tourNameTextView = tourView.findViewById(R.id.tour_name_et);
        tourAttractionsButton = tourView.findViewById(R.id.tour_add_attraction_btn);
        tourLocationEditText = tourView.findViewById(R.id.tour_location_et);
        tourCostEditText = tourView.findViewById(R.id.tour_cost_et);
        tourLengthEditText = tourView.findViewById(R.id.tour_length_et);
        tourUpdateButton = tourView.findViewById(R.id.tour_update_btn);
        tourShareButton = tourView.findViewById(R.id.tour_share_btn);
        coverImageView = tourView.findViewById(R.id.tour_cover_iv);
        tourCoverButton = tourView.findViewById(R.id.tour_cover_btn);

        tourNameTextView.setText(tour.getName());
        tourLocationEditText.setText("Location:" + tourViewModel.getSelectedTour().getLocation());
        tourCostEditText.setText("Cost($): " + Float.toString(tourViewModel.getSelectedTour().getCost()));
        tourLengthEditText.setText("Length: " + Long.toString(tourViewModel.getSelectedTour().getLength()));

        // When the button is clicked, switch to the AddAttractionFragment
        tourAttractionsButton.setOnClickListener(v -> {
            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new AddAttractionFragment(), "AddAttractionFragment");
            ft.addToBackStack("AddAttractionFragment").commit();
        });

        // set up fields to be made visible or invisible
        tourNameTextView.setEnabled(false);
        tourLocationEditText.setEnabled(false);
        tourCostEditText.setEnabled(false);
        tourLengthEditText.setEnabled(false);
        tourUpdateButton.setVisibility(View.INVISIBLE);
        tourShareButton.setVisibility(View.INVISIBLE);
        tourAttractionsButton.setVisibility(View.INVISIBLE);
        tourCoverButton.setVisibility(View.INVISIBLE);
        
        tourCoverButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            int PICK_IMAGE = 1;
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        Glide.with(getContext()).load(tour.getCoverImageURI()).into(coverImageView);

        // Check if the user is logged in to identify if the tour belongs to them
        if (MainActivity.user != null) {
            tourIsUsers();
        }

        // set up the recycler view of attractions
        configureRecyclerViews(tourView);
        configureSwipeRefreshLayouts(tourView);

        setupUpdateTourButton(tourView);

        return tourView;
    }

    /**
     * Configure the recycler view
     *
     * @param view current view
     */
    public void configureRecyclerViews(View view) {

        // Get our recycler view from the layout
        RecyclerView attractionsView = view.findViewById(R.id.tour_attractions_rv);

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
     * Check if the tour belongs to the current user and make fields visible if so
     *
     */
    public void tourIsUsers(){

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Pull out the UID's of each tour that belongs to this user
        List<String> usersToursUIDs = new ArrayList<>();
        if (!MainActivity.user.getTours().isEmpty()) {
            for (DocumentReference documentReference : MainActivity.user.getTours()) {
                usersToursUIDs.add(documentReference.getId());
            }
        }

        if (usersToursUIDs.contains(tourViewModel.getSelectedTour().getTourUID())) {
            tourAttractionsButton.setVisibility(View.VISIBLE);
            tourNameTextView.setEnabled(true);
            tourLocationEditText.setEnabled(true);
            tourCostEditText.setEnabled(true);
            tourLengthEditText.setEnabled(true);
            tourUpdateButton.setVisibility(View.VISIBLE);
            tourCoverButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(resultCode == Activity.RESULT_OK) {
            assert imageReturnedIntent != null;

            Glide.with(this)
                    .load(imageReturnedIntent.getData())
                    .placeholder(R.drawable.default_image)
                    .into(coverImageView);
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
                            })
                            .addOnFailureListener(exception -> {
                                Log.e(TAG, "Error retrieving uri for image: " + imageUUID + " in cloud storage, " + exception.getMessage());
                            });
                });
    }

    public void setupUpdateTourButton(View view) {

        Button editTourUpdateButton = view.findViewById(R.id.tour_update_btn);

        editTourUpdateButton.setOnClickListener(view1 -> {

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            EditText tourNameEditText = view.findViewById(R.id.tour_name_et);
            EditText tourLocationEditText = view.findViewById(R.id.tour_location_et);
            EditText tourCostEditText = view.findViewById(R.id.tour_cost_et);
            EditText tourLengthEditText = view.findViewById(R.id.tour_length_et);

            if (tourNameEditText.getText().toString().equals("") ||
            tourLocationEditText.getText().toString().equals("") ||
            tourCostEditText.getText().toString().equals("") ||
            tourLengthEditText.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Not all fields entered", Toast.LENGTH_SHORT).show();
                return;
            }

            tourViewModel.getSelectedTour().setName(tourNameEditText.getText().toString());
            tourViewModel.getSelectedTour().setLocation(tourLocationEditText.getText().toString());
            tourViewModel.getSelectedTour().setCost(Float.parseFloat(tourCostEditText.getText().toString()));
            tourViewModel.getSelectedTour().setLength(Long.parseLong(tourLengthEditText.getText().toString()));

            db.collection("Tours").document(tourViewModel.getSelectedTour().getTourUID())
                    .set(tourViewModel.getSelectedTour())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Successfully updated tour in firestore");


                            Toast.makeText(getContext(), "Successfully Updated Tour", Toast.LENGTH_SHORT).show();

                        }
                    });
        });
    }
}

