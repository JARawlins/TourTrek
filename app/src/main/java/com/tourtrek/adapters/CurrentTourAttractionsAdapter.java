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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Attraction;
import com.tourtrek.data.Tour;

import java.util.ArrayList;
import java.util.List;

public class CurrentTourAttractionsAdapter extends RecyclerView.Adapter<CurrentTourAttractionsAdapter.CurrentAttractionsViewHolder> {

    private static final String TAG = "CurrentTourAttractionsAdapter";
    private List<Attraction> currentTourAttractionsDataSet;
    private Context context;
    private RecyclerView recyclerView;

    public static class CurrentAttractionsViewHolder extends RecyclerView.ViewHolder {

        public TextView attractionName;
        public TextView attractionLocation;

        public CurrentAttractionsViewHolder (View view) {
            super(view);
            this.attractionName = view.findViewById(R.id.item_attraction_name_tv);
            this.attractionLocation = view.findViewById(R.id.item_attraction_location_tv);
        }

    }

    public CurrentTourAttractionsAdapter (Context context) {
        this.currentTourAttractionsDataSet = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CurrentAttractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
        return new CurrentAttractionsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //@Override
    public void onBindViewHolder(@NonNull CurrentTourAttractionsAdapter.CurrentAttractionsViewHolder holder, int position) {

        ((MainActivity) context).findViewById(R.id.attractions_loading_container).setVisibility(View.VISIBLE);
        ((MainActivity) context).findViewById(R.id.tour_attractions_rv).setVisibility(View.INVISIBLE);

         holder.attractionName.setText(currentTourAttractionsDataSet.get(position).getName());
         holder.attractionLocation.setText(currentTourAttractionsDataSet.get(position).getLocation());

        if (position == getItemCount() - 1) {
            ((MainActivity) context).findViewById(R.id.personal_current_tours_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.personal_current_tours_rv).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return currentTourAttractionsDataSet.size();
    }

    /**
     * Adds a new item to our tours list
     *
     * @param newAttraction tour to be added
     */
    public void addNewData(Attraction newAttraction) {
        currentTourAttractionsDataSet.add(newAttraction);
        notifyDataSetChanged();
    }

    /**
     * Returns a tour at a specified index
     *
     * @param position index of tour to get
     *
     * @return tour at the position specified
     */
    public Attraction getAttraction(int position) {
        return currentTourAttractionsDataSet.get(position);
    }

    /**
     * Clean all elements of the recycler
     */
    public void clear() {
        currentTourAttractionsDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of tours to the recycler
     *
     * @param dataSet list of tours to add
     */
    public void addAll(List<Attraction> dataSet) {
        this.currentTourAttractionsDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void stopLoading() {
        if (((MainActivity) context).findViewById(R.id.attractions_loading_container) != null) {
            ((MainActivity) context).findViewById(R.id.attractions_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.tour_attractions_rv).setVisibility(View.VISIBLE);
        }
    }
}