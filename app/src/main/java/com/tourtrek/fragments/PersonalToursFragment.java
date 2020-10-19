package com.tourtrek.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.CurrentPersonalToursAdapter;
import com.tourtrek.adapters.FuturePersonalToursAdapter;
import com.tourtrek.adapters.PastPersonalToursAdapter;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.ItemClickSupport;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalToursFragment extends Fragment {

    private static final String TAG = "ToursFragment";
    private RecyclerView currentRecyclerView;
    private RecyclerView futureRecyclerView;
    private RecyclerView pastRecyclerView;
    private RecyclerView.Adapter currentTourAdapter;
    private RecyclerView.Adapter futureTourAdapter;
    private RecyclerView.Adapter pastTourAdapter;
    private RecyclerView.LayoutManager currentToursLayoutManager;
    private RecyclerView.LayoutManager futureToursLayoutManager;
    private RecyclerView.LayoutManager pastToursLayoutManager;
    private SwipeRefreshLayout currentSwipeRefreshLayout;
    private SwipeRefreshLayout futureSwipeRefreshLayout;
    private SwipeRefreshLayout pastSwipeRefreshLayout;
    private TourViewModel tourViewModel;
    private FirebaseAuth mAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Get the current nav backstack
        NavController navController = NavHostFragment.findNavController(this);

        // Display login screen if no user was previous logged in
        if (mAuth.getCurrentUser() == null || MainActivity.user == null) {
            navController.navigate(R.id.navigation_login);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View personalToursView = inflater.inflate(R.layout.fragment_personal_tours, container, false);

        // Initialize view model
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);

        if (MainActivity.user != null) {

            // Configure recycler views
            configureRecyclerViews(personalToursView);

            configureSwipeRefreshLayouts(personalToursView);

            configureOnClickRecyclerView();

            // TODO: This is where we will load the users past, present and future tours
        }

        return personalToursView;
    }

    public void configureRecyclerViews(View view) {

        // Current

            // Get our recycler view from the layout
            currentRecyclerView = view.findViewById(R.id.personal_current_tours_rv);

            // Improves performance because content does not change size
            currentRecyclerView.setHasFixedSize(true);

            // Only load 10 tours before loading more
            currentRecyclerView.setItemViewCacheSize(10);

            // Enable drawing cache
            currentRecyclerView.setDrawingCacheEnabled(true);
            currentRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            // User linear layout manager
            currentToursLayoutManager = new LinearLayoutManager(getContext());
            currentRecyclerView.setLayoutManager(currentToursLayoutManager);

            fetchToursAsync(0, "current");

            // Specify an adapter
            currentTourAdapter = new CurrentPersonalToursAdapter(getContext());
            currentRecyclerView.setAdapter(currentTourAdapter);

            currentRecyclerView
                    .getViewTreeObserver()
                    .addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
//                                    ((CurrentPersonalToursAdapter)currentTourAdapter).stopLoading();
                                }
                            });

        // Future

            // Get our recycler view from the layout
            futureRecyclerView = view.findViewById(R.id.personal_future_tours_rv);

            // Improves performance because content does not change size
            futureRecyclerView.setHasFixedSize(true);

            // Only load 10 tours before loading more
            futureRecyclerView.setItemViewCacheSize(10);

            // Enable drawing cache
            futureRecyclerView.setDrawingCacheEnabled(true);
            futureRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            // User linear layout manager
            futureToursLayoutManager = new LinearLayoutManager(getContext());
            futureRecyclerView.setLayoutManager(futureToursLayoutManager);

            fetchToursAsync(0, "future");

            // Specify an adapter
            futureTourAdapter = new FuturePersonalToursAdapter(getContext());
            futureRecyclerView.setAdapter(futureTourAdapter);

        // Past

            // Get our recycler view from the layout
            pastRecyclerView = view.findViewById(R.id.personal_past_tours_rv);

            // Improves performance because content does not change size
            pastRecyclerView.setHasFixedSize(true);

            // Only load 10 tours before loading more
            pastRecyclerView.setItemViewCacheSize(10);

            // Enable drawing cache
            pastRecyclerView.setDrawingCacheEnabled(true);
            pastRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            // User linear layout manager
            pastToursLayoutManager = new LinearLayoutManager(getContext());
            pastRecyclerView.setLayoutManager(pastToursLayoutManager);

            fetchToursAsync(0, "past");

            // Specify an adapter
            pastTourAdapter = new PastPersonalToursAdapter(getContext());
            pastRecyclerView.setAdapter(pastTourAdapter);

    }

    public void configureSwipeRefreshLayouts(View view) {

        // Current
            currentSwipeRefreshLayout = view.findViewById(R.id.personal_current_tours_srl);

            currentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchToursAsync(0, "current");
                }
            });

        // Future
            futureSwipeRefreshLayout = view.findViewById(R.id.personal_future_tours_srl);

            futureSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchToursAsync(0, "future");
                }
            });


        // Past
            pastSwipeRefreshLayout = view.findViewById(R.id.personal_past_tours_srl);

            pastSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchToursAsync(0, "past");
                }
            });

    }

    public void configureOnClickRecyclerView() {

        // Current
        ItemClickSupport.addTo(currentRecyclerView, R.layout.item_personal_tour)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Tour tour = ((CurrentPersonalToursAdapter) currentTourAdapter).getTour(position);

                        // Add the selected tour to the view model
                        tourViewModel.setSelectedTour(tour);

                        // TODO: This is where we will overlay the Tour fragment, which displays all information about the tour
                        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new TourFragment(), "TourFragment");
                        ft.addToBackStack("TourFragment").commit();
                    }
                });

        // Future
        ItemClickSupport.addTo(futureRecyclerView, R.layout.item_personal_tour)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Tour tour = ((FuturePersonalToursAdapter) futureTourAdapter).getTour(position);

                        // Add the selected tour to the view model
                        tourViewModel.setSelectedTour(tour);

                        // TODO: This is where we will overlay the Tour fragment, which displays all information about the tour
                        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new TourFragment(), "TourFragment");
                        ft.addToBackStack("TourFragment").commit();
                    }
                });

        // Past
        ItemClickSupport.addTo(pastRecyclerView, R.layout.item_personal_tour)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Tour tour = ((PastPersonalToursAdapter) pastTourAdapter).getTour(position);

                        // Add the selected tour to the view model
                        tourViewModel.setSelectedTour(tour);

                        // TODO: This is where we will overlay the Tour fragment, which displays all information about the tour
                        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new TourFragment(), "TourFragment");
                        ft.addToBackStack("TourFragment").commit();
                    }
                });
    }

    /**
     * Retrieve all tours belonging to this user
     *
     * @param page limit the number of pages to load
     * @param type type of tours to fetch
     */
    private void fetchToursAsync(int page, String type) {

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference toursReference = db.collection("Tours");

        // Pull out the UID's from each documentReference
        List<String> usersToursUIDs = new ArrayList<>();
        for (DocumentReference documentReference : MainActivity.user.getTours()) {
            usersToursUIDs.add(documentReference.getId());
        }

        // Grab all tours in order to query
        db.collection("Tours")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Log.w(TAG, "No documents found in the Tours collection");
                        }
                        else {
                            // Final list of tours for this category
                            List<Tour> usersTours = new ArrayList<>();

                            // Go through each document and compare the dates
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                                // First check that the document belongs to the user
                                if (usersToursUIDs.contains(document.getId())) {

                                    Timestamp tourStartDate = (Timestamp) document.get("startDate");
                                    Timestamp now = Timestamp.now();
                                    Long length = (Long)document.get("length");

                                    int test = tourStartDate.compareTo(now);

                                    // Next query based on timestamp
                                    if (type.equals("current")) { // TODO: filter based on tour length and timestamp
//                                        usersTours.add(document.toObject(Tour.class));
                                    }
                                    else if (type.equals("future") && tourStartDate.compareTo(now) > 0) {
                                        usersTours.add(document.toObject(Tour.class));
                                    }
                                    else if (type.equals("past") && tourStartDate.compareTo(now) < 0) {
                                        usersTours.add(document.toObject(Tour.class));
                                    }
                                }
                            }

                            if (type.equals("current")) {
                                ((CurrentPersonalToursAdapter) currentTourAdapter).clear();
                                ((CurrentPersonalToursAdapter) currentTourAdapter).addAll(usersTours);
                                currentSwipeRefreshLayout.setRefreshing(false);
                            }
                            else if (type.equals("future")) {
                                ((FuturePersonalToursAdapter) futureTourAdapter).clear();
                                ((FuturePersonalToursAdapter) futureTourAdapter).addAll(usersTours);
                                futureSwipeRefreshLayout.setRefreshing(false);
                            }
                            else if (type.equals("past")) {
                                ((PastPersonalToursAdapter) pastTourAdapter).clear();
                                ((PastPersonalToursAdapter) pastTourAdapter).addAll(usersTours);
                                pastSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }
                });



//        query
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//
//                        ArrayList<Tour> tours = new ArrayList<>();
//
//                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                            tours.add(documentSnapshot.toObject(Tour.class));
//                        }
//
//                        Log.i(TAG, "Successfully retrieved " + tours.size() + " tours from the firestore");
//
//                        if (type.equals("current")) {
//                            ((CurrentPersonalToursAdapter)currentTourMarketAdapter).clear();
//                            ((CurrentPersonalToursAdapter)currentTourMarketAdapter).addAll(tours);
//                            currentSwipeRefreshLayout.setRefreshing(false);
//                        }
//                        else if (type.equals("future")) {
//                            ((FuturePersonalToursAdapter)futureTourMarketAdapter).clear();
//                            ((FuturePersonalToursAdapter)futureTourMarketAdapter).addAll(tours);
//                            futureSwipeRefreshLayout.setRefreshing(false);
//                        }
//                        else if (type.equals("past")) {
//                            ((PastPersonalToursAdapter)pastTourMarketAdapter).clear();
//                            ((PastPersonalToursAdapter)pastTourMarketAdapter).addAll(tours);
//                            pastSwipeRefreshLayout.setRefreshing(false);
//                        }
//
//                    }
//                    else {
//                        Log.w(TAG, "Error retrieving tours from firestore: ", task.getException());
//                    }
//                }
//            });
    }

}