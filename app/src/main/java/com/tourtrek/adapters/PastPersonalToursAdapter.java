package com.tourtrek.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Tour;

import java.util.ArrayList;
import java.util.List;

public class PastPersonalToursAdapter extends RecyclerView.Adapter<PastPersonalToursAdapter.PastPersonalToursViewHolder> {

    private static final String TAG = "PastToursAdapter";
    private final List<Tour> pastPersonalToursDataSet;
    private final Context context;

    public static class PastPersonalToursViewHolder extends RecyclerView.ViewHolder {

        public TextView tourName;
        public TextView location;

        public PastPersonalToursViewHolder(View view) {
            super(view);
            this.tourName = view.findViewById(R.id.item_personal_tour_name);
            this.location = view.findViewById(R.id.item_personal_tour_location);
        }

    }

    public PastPersonalToursAdapter(Context context) {
        this.pastPersonalToursDataSet = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public PastPersonalToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_tour, parent, false);
        return new PastPersonalToursViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull PastPersonalToursViewHolder holder, int position) {

        ((MainActivity) context).findViewById(R.id.personal_past_tours_loading_container).setVisibility(View.VISIBLE);
        ((MainActivity) context).findViewById(R.id.personal_past_tours_rv).setVisibility(View.INVISIBLE);

        holder.tourName.setText(pastPersonalToursDataSet.get(position).getName());
        holder.location.setText(pastPersonalToursDataSet.get(position).getLocation());

        ((MainActivity) context).findViewById(R.id.personal_past_tours_loading_container).setVisibility(View.INVISIBLE);
        ((MainActivity) context).findViewById(R.id.personal_past_tours_rv).setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return pastPersonalToursDataSet.size();
    }

    /**
     * Adds a new item to our tours list
     *
     * @param newTour tour to be added
     */
    public void addNewData(Tour newTour) {
        pastPersonalToursDataSet.add(newTour);
        notifyDataSetChanged();
    }

    /**
     * Returns a tour at a specified index
     *
     * @param position index of tour to get
     *
     * @return tour at the position specified
     */
    public Tour getTour(int position) {
        return pastPersonalToursDataSet.get(position);
    }

    /**
     * Clean all elements of the recycler
     */
    public void clear() {
        pastPersonalToursDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of tours to the recycler
     *
     * @param dataSet list of tours to add
     */
    public void addAll(List<Tour> dataSet) {
        this.pastPersonalToursDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    /**
     * Stop the loading of the progress bar for the recycler view
     */
    public void stopLoading() {
        if (((MainActivity) context).findViewById(R.id.personal_past_tours_loading_container) != null) {
            ((MainActivity) context).findViewById(R.id.personal_past_tours_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.personal_past_tours_rv).setVisibility(View.VISIBLE);
        }
    }
}
