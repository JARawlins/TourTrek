package com.tourtrek.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.adapters.TourMarketAdapter;
import com.tourtrek.data.Tour;
import com.tourtrek.utilities.ItemClickSupport;
import com.tourtrek.viewModels.TourViewModel;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddTourFragment extends Fragment {

    private static final String TAG = "AddTourFragment";
    private TourViewModel tourViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter addTourAdapter;

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
        tourViewModel = new ViewModelProvider(this.getActivity()).get(TourViewModel.class);

        configureRecyclerView(addTourView);
        configureOnClickRecyclerView();

        return addTourView;
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
                        Tour tour = ((TourMarketAdapter)addTourAdapter).getTour(position);

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


        // Specify an adapter
        addTourAdapter = new TourMarketAdapter(getContext());
        recyclerView.setAdapter(addTourAdapter);

    }





    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Add a Tour");
    }
}