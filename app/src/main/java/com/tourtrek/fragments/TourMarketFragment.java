package com.tourtrek.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.TourMarketAdapter;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.ItemClickSupport;
import com.tourtrek.viewModels.TourMarketViewModel;

import java.util.ArrayList;

public class TourMarketFragment extends Fragment {

    private static final String TAG = "TourMarketFragment";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter tourMarketAdapter;
    private RecyclerView.LayoutManager tourMarketLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TourMarketViewModel tourMarketViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tourMarketView = inflater.inflate(R.layout.fragment_tour_market, container, false);

        // Initialize view model
        tourMarketViewModel = new ViewModelProvider(this.getActivity()).get(TourMarketViewModel.class);

        configureRecyclerView(tourMarketView);
        configureSwipeRefreshLayout(tourMarketView);
        configureOnClickRecyclerView();

        return tourMarketView;
    }

    /**
     * Enables the click listener for each item in our recycler view
     */
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(recyclerView, R.layout.item_tour)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        Tour tour = ((TourMarketAdapter)tourMarketAdapter).getTour(position);

                        // Add the selected tour to the view model
                        tourMarketViewModel.setSelectedTour(tour);

                        // TODO: This is where we will overlay the Tour fragment, which displays all information about the tour
                        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new TourFragment(), "TourFragment");
                        ft.addToBackStack("TourFragment").commit();
                    }
                });
    }

    /**
     * Configure the recycler view
     *
     * @param tourMarketView current view
     */
    private void configureRecyclerView(View tourMarketView) {

        // Get our recycler view from the layout
        recyclerView = tourMarketView.findViewById(R.id.tour_market_rv);

        // Improves performance because content does not change size
        recyclerView.setHasFixedSize(true);

        // Only load 10 tours before loading more
        recyclerView.setItemViewCacheSize(10);

        // Enable drawing cache
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // User linear layout manager
        tourMarketLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(tourMarketLayoutManager);

        fetchToursAsync(0);

        // Specify an adapter
        tourMarketAdapter = new TourMarketAdapter(getContext());
        recyclerView.setAdapter(tourMarketAdapter);

    }

    /**
     * Configure the swipe down to refresh function of our recycler view
     *
     * @param tourMarketView current view
     */
    private void configureSwipeRefreshLayout(View tourMarketView) {

        swipeRefreshLayout = tourMarketView.findViewById(R.id.tour_market_srl);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchToursAsync(0);
            }
        });
    }

    /**
     * Retrieve all publicly facing tours from firestore
     *
     * @param page limit the number of pages to load
     */
    private void fetchToursAsync(int page) {

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference toursReference = db.collection("Tours");

        // Setup basic query information
        Query query = toursReference.whereEqualTo("publiclyAvailable", true);

        db.collection("Tours")
                .whereEqualTo("publiclyAvailable", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Tour> tours = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                tours.add(documentSnapshot.toObject(Tour.class));
                            }

                            Log.i(TAG, "Successfully retrieved " + tours.size() + " tours from the firestore");

                            ((TourMarketAdapter)tourMarketAdapter).clear();

                            ((TourMarketAdapter)tourMarketAdapter).addAll(tours);

                            swipeRefreshLayout.setRefreshing(false);
                        }
                        else {
                            Log.w(TAG, "Error retrieving tours from firestore: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Tour Market");
    }
}