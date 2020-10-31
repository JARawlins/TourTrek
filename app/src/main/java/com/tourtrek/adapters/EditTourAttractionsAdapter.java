package com.tourtrek.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tourtrek.R;
import com.tourtrek.activities.MainActivity;
import com.tourtrek.data.Attraction;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class EditTourAttractionsAdapter extends RecyclerView.Adapter<EditTourAttractionsAdapter.CurrentAttractionsViewHolder> {

    private static final String TAG = "CurrentTourAttractionsAdapter";
    private List<Attraction> editTourAttractionsDataSet;
    private Context context;

    public static class CurrentAttractionsViewHolder extends RecyclerView.ViewHolder {

        public TextView attractionName;
        public TextView attractionLocation;

        public CurrentAttractionsViewHolder (View view) {
            super(view);
            this.attractionName = view.findViewById(R.id.item_attraction_name_tv);
            this.attractionLocation = view.findViewById(R.id.item_attraction_location_tv);
        }

    }

    public EditTourAttractionsAdapter(Context context) {
        this.editTourAttractionsDataSet = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public CurrentAttractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attraction, parent, false);
        return new CurrentAttractionsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull EditTourAttractionsAdapter.CurrentAttractionsViewHolder holder, int position) {

        ((MainActivity) context).findViewById(R.id.edit_tour_2_attractions_srl).setVisibility(View.INVISIBLE);

         holder.attractionName.setText(editTourAttractionsDataSet.get(position).getName());
         holder.attractionLocation.setText(editTourAttractionsDataSet.get(position).getLocation());

        ((MainActivity) context).findViewById(R.id.edit_tour_2_attractions_srl).setVisibility(View.VISIBLE);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return editTourAttractionsDataSet.size();
    }

    /**
     * Adds a new item to our recycler view
     *
     * @param attraction item to be added
     */
    public void addNewData(Attraction attraction) {

        if (!this.editTourAttractionsDataSet.contains(attraction)) {
            this.editTourAttractionsDataSet.add(attraction);
            notifyDataSetChanged();
        }

    }

    /**
     * Add a list of items to the recycler view
     *
     * @param attractions list of items to add
     */
    public void addAll(List<Attraction> attractions) {
        this.editTourAttractionsDataSet.addAll(attractions);
        notifyDataSetChanged();
    }

    /**
     * Returns an item from the recycler view
     *
     * @param position index of item to get
     *
     * @return item at the position specified
     */
    public Attraction getData(int position) {
        return editTourAttractionsDataSet.get(position);
    }

    /**
     * Clean all elements of the recycler view
     */
    public void clear() {
        editTourAttractionsDataSet.clear();
        notifyDataSetChanged();
    }

    /**
     * Stop the loading of the progress bar for the recycler view
     */
    public void stopLoading() {
        if (((MainActivity) context).findViewById(R.id.attractions_loading_container) != null) {
            ((MainActivity) context).findViewById(R.id.attractions_loading_container).setVisibility(View.INVISIBLE);
            ((MainActivity) context).findViewById(R.id.tour_attractions_rv).setVisibility(View.VISIBLE);
        }
    }
}