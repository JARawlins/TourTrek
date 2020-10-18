package com.tourtrek.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tourtrek.R;
import com.tourtrek.data.Tour;
import com.tourtrek.fragments.TourMarketFragment;

import java.util.ArrayList;
import java.util.List;

public class TourMarketAdapter extends RecyclerView.Adapter<TourMarketAdapter.TourMarketViewHolder> {

    private List<Tour> toursDataSet;
    private Context context;

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

    /**
     * Adapter Constructor
     *
     * @param toursDataSet tours to set
     */
    public TourMarketAdapter(List<Tour> toursDataSet, Context context) {
        this.toursDataSet = toursDataSet;
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
        holder.tourName.setText(toursDataSet.get(position).getName());

        holder.coverImage.setImageDrawable(null);
        Picasso.get().load(toursDataSet.get(position).getCoverImageURI()).into(holder.coverImage);
        holder.coverImage.setClipToOutline(true); // TODO: I can't figure out the equivalent in xml

        holder.location.setText(toursDataSet.get(position).getLocation());
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
     * @param toursDataSet list of tour to add
     */
    public void addAll(List<Tour> toursDataSet) {
        this.toursDataSet.addAll(toursDataSet);
        notifyDataSetChanged();
    }
}
