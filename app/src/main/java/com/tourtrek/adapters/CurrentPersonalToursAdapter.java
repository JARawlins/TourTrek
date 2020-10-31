package com.tourtrek.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Tour;

import java.util.ArrayList;
import java.util.List;

public class CurrentPersonalToursAdapter extends RecyclerView.Adapter<CurrentPersonalToursAdapter.CurrentPersonalToursViewHolder> {

    private static final String TAG = "CurrentPersonalToursAdapter";
    private final List<Tour> currentPersonalToursDataSet;
    private final Context context;

    public static class CurrentPersonalToursViewHolder extends RecyclerView.ViewHolder {

        public TextView tourName;
        public TextView location;

        public CurrentPersonalToursViewHolder(View view) {
            super(view);
            this.tourName = view.findViewById(R.id.item_personal_tour_name);
            this.location = view.findViewById(R.id.item_personal_tour_location);
        }

    }

    public CurrentPersonalToursAdapter(Context context) {
        this.currentPersonalToursDataSet = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CurrentPersonalToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_tour, parent, false);
        return new CurrentPersonalToursViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CurrentPersonalToursAdapter.CurrentPersonalToursViewHolder holder, int position) {

        ((MainActivity) context).findViewById(R.id.personal_current_tours_loading_container).setVisibility(View.VISIBLE);
        ((MainActivity) context).findViewById(R.id.personal_current_tours_rv).setVisibility(View.INVISIBLE);

        holder.tourName.setText(currentPersonalToursDataSet.get(position).getName());
        holder.location.setText(currentPersonalToursDataSet.get(position).getLocation());

        ((MainActivity) context).findViewById(R.id.personal_current_tours_loading_container).setVisibility(View.INVISIBLE);
        ((MainActivity) context).findViewById(R.id.personal_current_tours_rv).setVisibility(View.VISIBLE);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return currentPersonalToursDataSet.size();
    }

    /**
     * Adds a new item to our tours list
     *
     * @param newTour tour to be added
     */
    public void addNewData(Tour newTour) {
        currentPersonalToursDataSet.add(newTour);
        notifyDataSetChanged();
    }

    /**
     * Returns a tour at a specified index
     *
     * @param position index of tour to get
     *
     * @return tour at the position specified
     */
    public Tour getData(int position) {
        return currentPersonalToursDataSet.get(position);
    }

    /**
     * Clean all elements of the recycler
     */
    public void clear() {
        currentPersonalToursDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of tours to the recycler
     *
     * @param dataSet list of tours to add
     */
    public void addAll(List<Tour> dataSet) {
        this.currentPersonalToursDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    /**
     * Stop the loading of the progress bar for the recycler view
     */
    public void stopLoading() {
        if (((MainActivity) context).findViewById(R.id.personal_current_tours_loading_container) != null) {
            ((MainActivity) context).findViewById(R.id.personal_current_tours_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.personal_current_tours_rv).setVisibility(View.VISIBLE);
        }
    }
}
