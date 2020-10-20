package com.tourtrek.fragments;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Tour;
import com.tourtrek.viewModels.TourViewModel;

public class TourFragment extends Fragment {

    private static final String TAG = "TourFragment";
    private TourViewModel tourViewModel;
    private Tour tour;

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

        return tourView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(tour.getName());
    }
}