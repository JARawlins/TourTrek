package com.tourtrek.fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.CurrentTourAttractionsAdapter;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;

public class TourFragment extends Fragment {

    private static final String TAG = "TourFragment";
    private TourViewModel tourViewModel;
    private Tour tour;
    private RecyclerView attractionsView;
    private RecyclerView.Adapter attractionsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        View tourView = inflater.inflate(R.layout.tour_fragment, container, false);
        // Grab the tour that was selected
        this.tour = tourViewModel.getSelectedTour();
        TextView tourNameTextView = tourView.findViewById(R.id.tour_tour_name_tv);
        tourNameTextView.setText(tour.getName());
        ImageView tourCoverImageView = tourView.findViewById(R.id.tour_cover_iv);
        Glide.with(getContext()).load(tour.getCoverImageURI()).into(tourCoverImageView);
        // set up the recycler view of attractions
        configureRecyclerViews(tourView);
        // Create a button which directs to addAttractionFragment when pressed
        Button tour_attractions_btn = tourView.findViewById(R.id.tour_attractions_btn);
        // When the button is clicked, switch to the AddAttractionFragment
        tour_attractions_btn.setOnClickListener(v -> {
            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new AddAttractionFragment(), "AddAttractionFragment");
            ft.addToBackStack("AdAttractionFragment").commit();
        });
        return tourView;
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
//                        currentSwipeRefreshLayout.setRefreshing(false);

                    }
                });
    }

//    /**
//     * Configure the swipe down to refresh function of our recycler view
//     *
//     * @param view current view
//     */
//    public void configureSwipeRefreshLayouts(View view) {
//
//        // Current
//        swipeRefreshLayout = view.findViewById(R.id.personal_current_tours_srl);
//        currentSwipeRefreshLayout.setOnRefreshListener(() -> fetchToursAsync("current"));
//
//        // Future
//        futureSwipeRefreshLayout = view.findViewById(R.id.personal_future_tours_srl);
//        futureSwipeRefreshLayout.setOnRefreshListener(() -> fetchToursAsync("future"));
//
//        // Past
//        pastSwipeRefreshLayout = view.findViewById(R.id.attractions_srl);
//        pastSwipeRefreshLayout.setOnRefreshListener(() -> fetchToursAsync("past"));
//
//    }

}

