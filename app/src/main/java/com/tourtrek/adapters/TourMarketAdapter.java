package com.tourtrek.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Tour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TourMarketAdapter extends RecyclerView.Adapter<TourMarketAdapter.TourMarketViewHolder> implements Filterable{

    private static final String TAG = "TourMarketAdapter";
    private List<Tour> toursDataSet;
    private final Context context;
    private List<Tour> toursDataSetCopy;

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tour> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(toursDataSetCopy);
            } else {
                String key = constraint.toString().toLowerCase().trim();
                for(Tour tour: toursDataSetCopy){
                    if(tour.getName().toLowerCase().contains(key)){
                        filteredList.add(tour);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            toursDataSet.clear();
            toursDataSet.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


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
        this.toursDataSetCopy = new ArrayList<>(toursDataSet);
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

        Glide.with(context)
                .load(toursDataSet.get(position).getCoverImageURI())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.w(TAG, "Error: Tour cover image not loaded");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ((MainActivity) context).findViewById(R.id.tour_market_loading_container).setVisibility(View.INVISIBLE);
                        ((MainActivity) context).findViewById(R.id.tour_market_rv).setVisibility(View.VISIBLE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.coverImage.setClipToOutline(true);
                        }
                        return false;
                    }
                })
                .into(holder.coverImage);
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

    public void copyTours(List<Tour> dataSet){
        this.toursDataSetCopy.addAll(dataSet);
    }

    public List<Tour> getToursDataSet() {
        return toursDataSetCopy;
    }

}
