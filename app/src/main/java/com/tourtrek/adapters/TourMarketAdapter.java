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

public class TourMarketAdapter extends RecyclerView.Adapter<TourMarketAdapter.TourMarketViewHolder> {

    private static final String TAG = "TourMarketAdapter";
    private final List<Tour> toursDataSet;
    private final Context context;

    public static class TourMarketViewHolder extends RecyclerView.ViewHolder {

        public TextView tourName;
        public ImageView coverImage;
        public TextView location;

        public TourMarketViewHolder(View view) {
            super(view);
            this.tourName = view.findViewById(R.id.item_tour_name);
            this.coverImage = view.findViewById(R.id.item_tour_cover_iv);
            this.location = view.findViewById(R.id.item_tour_location);
        }

    }

    /**
     * Empty constructor
     */
    public TourMarketAdapter(Context context) {
        this.toursDataSet = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public TourMarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tour, parent, false);
        return new TourMarketViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull TourMarketViewHolder holder, int position) {

        ((MainActivity) context).findViewById(R.id.tour_market_loading_container).setVisibility(View.VISIBLE);
        ((MainActivity) context).findViewById(R.id.tour_market_rv).setVisibility(View.INVISIBLE);

        holder.tourName.setText(toursDataSet.get(position).getName());
        holder.location.setText(toursDataSet.get(position).getLocation());

        Picasso.get()
                .load(toursDataSet.get(position).getCoverImageURI())
                .into(holder.coverImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ((MainActivity) context).findViewById(R.id.tour_market_loading_container).setVisibility(View.INVISIBLE);
                        ((MainActivity) context).findViewById(R.id.tour_market_rv).setVisibility(View.VISIBLE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.coverImage.setClipToOutline(true);
                        }

                    }
                    @Override
                    public void onError(Exception e) {
                        Log.w(TAG, "Error: Tour cover image not loaded");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return toursDataSet.size();
    }

    /**
     * Adds a new item to our tours list
     *
     * @param newTour tour to be added
     */
    public void addNewData(Tour newTour) {
        toursDataSet.add(newTour);
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
        return toursDataSet.get(position);
    }

    /**
     * Clean all elements of the recycler
     */
    public void clear() {
        toursDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of tours to the recycler
     *
     * @param dataSet list of tours to add
     */
    public void addAll(List<Tour> dataSet) {
        this.toursDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }
}
