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

public class FuturePersonalToursAdapter extends RecyclerView.Adapter<FuturePersonalToursAdapter.FuturePersonalToursViewHolder> {

    private static final String TAG = "FutureToursAdapter";
    private final List<Tour> futurePersonalToursDataSet;
    private final Context context;

    public static class FuturePersonalToursViewHolder extends RecyclerView.ViewHolder {

        public TextView tourName;
        public TextView location;

        public FuturePersonalToursViewHolder(View view) {
            super(view);
            this.tourName = view.findViewById(R.id.item_personal_tour_name);
            this.location = view.findViewById(R.id.item_personal_tour_location);
        }
    }

    public FuturePersonalToursAdapter(Context context) {
        this.futurePersonalToursDataSet = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public FuturePersonalToursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_tour, parent, false);
        return new FuturePersonalToursViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull FuturePersonalToursAdapter.FuturePersonalToursViewHolder holder, int position) {

        ((MainActivity) context).findViewById(R.id.personal_future_tours_loading_container).setVisibility(View.VISIBLE);
        ((MainActivity) context).findViewById(R.id.personal_future_tours_rv).setVisibility(View.INVISIBLE);

        holder.tourName.setText(futurePersonalToursDataSet.get(position).getName());
        holder.location.setText(futurePersonalToursDataSet.get(position).getLocation());

        if (position == getItemCount() - 1) {
            ((MainActivity) context).findViewById(R.id.personal_future_tours_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.personal_future_tours_rv).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return futurePersonalToursDataSet.size();
    }

    /**
     * Adds a new item to our tours list
     *
     * @param newTour tour to be added
     */
    public void addNewData(Tour newTour) {
        futurePersonalToursDataSet.add(newTour);
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
        return futurePersonalToursDataSet.get(position);
    }

    /**
     * Clean all elements of the recycler
     */
    public void clear() {
        futurePersonalToursDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of tours to the recycler
     *
     * @param dataSet list of tours to add
     */
    public void addAll(List<Tour> dataSet) {
        this.futurePersonalToursDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    /**
     * Stops the loading of the progress bar
     */
    public void stopLoading() {
        if (((MainActivity) context).findViewById(R.id.personal_future_tours_loading_container) != null) {
            ((MainActivity) context).findViewById(R.id.personal_future_tours_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.personal_future_tours_rv).setVisibility(View.VISIBLE);
        }
    }
}
