package com.tourtrek.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
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
import com.tourtrek.data.TourLengthSorter;
import com.tourtrek.data.TourLocationSorter;
import com.tourtrek.data.TourNameSorter;
import com.tourtrek.utilities.ItemClickSupport;
import com.tourtrek.viewModels.TourViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TourMarketFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "TourMarketFragment";
    private RecyclerView recyclerView;
    private TourMarketAdapter tourMarketAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TourViewModel tourViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tourMarketView = inflater.inflate(R.layout.fragment_tour_market, container, false);

        // Initialize view model
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);
        Spinner spinner = tourMarketView.findViewById(R.id.tour_market_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.categories,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

                        // Reference to the current tour selected
                        Tour tour = tourMarketAdapter.getTour(position);

                        // Add the selected tour to the view model so we can access the tour inside the fragment
                        tourViewModel.setSelectedTour(tour);

                        // Display the tour selected
                        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                        ft.replace(R.id.nav_host_fragment, new TourFragment(), "TourFragment");
                        ft.addToBackStack("TourFragment").commit();
                    }
                });
    }

    /**
     * Configure the recycler view
     *
     * @param view current view
     */
    private void configureRecyclerView(View view) {

        // Get our recycler view from the layout
        recyclerView = view.findViewById(R.id.tour_market_rv);

        // Improves performance because content does not change size
        recyclerView.setHasFixedSize(true);

        // Only load 10 tours before loading more
        recyclerView.setItemViewCacheSize(10);

        // Enable drawing cache
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        // User linear layout manager
        RecyclerView.LayoutManager tourMarketLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(tourMarketLayoutManager);

        // Fetch all the public tours
        fetchToursAsync();

        // Specify an adapter
        tourMarketAdapter = new TourMarketAdapter(getContext());
        recyclerView.setAdapter(tourMarketAdapter);

    }

    /**
     * Configure the swipe down to refresh function of our recycler view
     *
     * @param view current view
     */
    private void configureSwipeRefreshLayout(View view) {

        swipeRefreshLayout = view.findViewById(R.id.tour_market_srl);
        swipeRefreshLayout.setOnRefreshListener(this::fetchToursAsync);
    }

    /**
     * Retrieve all publicly facing tours from firestore
     */
    private void fetchToursAsync() {

        // Get instance of firestore
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Setup collection reference
        CollectionReference toursCollection = db.collection("Tours");

        // Setup basic query information
        Query query = toursCollection.whereEqualTo("publiclyAvailable", true);

        // Query database
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            // For holding all our tours
                            ArrayList<Tour> tours = new ArrayList<>();

                            // Convert each document into its object
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                tours.add(documentSnapshot.toObject(Tour.class));
                            }

                            Log.i(TAG, "Successfully retrieved " + tours.size() + " tours from the firestore");

                            // Clear and add tours
                            (tourMarketAdapter).clear();
                            (tourMarketAdapter).addAll(tours);
                            tourMarketAdapter.copyTours(tours);

                            // Stop showing refresh decorator
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        else {
                            Log.w(TAG, "Error retrieving tours from firestore: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tour_market_search_menu, menu);
        MenuItem item = menu.findItem(R.id.tour_market_search_itm);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<Tour> data = new ArrayList<>(tourMarketAdapter.getToursDataSet());
                List<Tour> filteredList = new ArrayList<>();
                if (query == null || query.length() == 0) {
                    filteredList.addAll(data);
                    tourMarketAdapter.clear();
                    tourMarketAdapter.addAll(filteredList);
                } else {
                    String key = query.toLowerCase();
                    for(Tour tour: data){
                        if(tour.getName().toLowerCase().contains(key)){
                            filteredList.add(tour);
                        }
                    }
                }
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(filteredList);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Tour> data = new ArrayList<>(tourMarketAdapter.getToursDataSet());
                List<Tour> filteredList = new ArrayList<>();
                if (newText == null || newText.length() == 0) {
                    filteredList.addAll(data);
                    tourMarketAdapter.clear();
                    tourMarketAdapter.addAll(filteredList);
                } else {
                    String key = newText.toLowerCase();
                    for(Tour tour: data){
                        if(tour.getName().toLowerCase().contains(key)){
                            filteredList.add(tour);
                        }
                    }
                }
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(filteredList);

                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Tour Market");
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<Tour> data = new ArrayList<>(tourMarketAdapter.getToursDataSet());
        String key = (String) parent.getItemAtPosition(position);
        switch (key){
            case "Name Ascending":
                List<Tour> temp = new ArrayList<>(data);
                temp.sort(new TourNameSorter());
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp);
                break;
            case "Location Ascending":
                List<Tour> temp2 = new ArrayList<>(data);
                temp2.sort(new TourLocationSorter());
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp2);
                break;
            case "Duration Ascending":
                List<Tour> temp3 = new ArrayList<>(data);
                temp3.sort(new TourLengthSorter());
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp3);
                break;
            case "Name Descending":
                List<Tour> temp4 = new ArrayList<>(data);
                temp4.sort(new TourNameSorter());
                Collections.reverse(temp4);
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp4);
                break;
            case "Location Descending":
                List<Tour> temp5 = new ArrayList<>(data);
                temp5.sort(new TourLocationSorter());
                Collections.reverse(temp5);
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp5);
                break;
            case "Duration Descending":
                System.out.println(key);
                List<Tour> temp6 = new ArrayList<>(data);
                temp6.sort(new TourLengthSorter());
                Collections.reverse(temp6);
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp6);
                break;
            default:
                List<Tour> temp0 = new ArrayList<>(data);
                tourMarketAdapter.clear();
                tourMarketAdapter.addAll(temp0);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

