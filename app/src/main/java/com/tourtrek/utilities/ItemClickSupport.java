package com.tourtrek.utilities;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemClickSupport {

    private final RecyclerView tourMarketRecyclerView;

    private OnItemClickListener tourMarketOnItemClickListener;

    private OnItemLongClickListener tourMarketOnItemLongClickListener;

    private int tourMarketItemID;

    private View.OnClickListener tourMarketOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (tourMarketOnItemClickListener != null) {

                RecyclerView.ViewHolder holder = tourMarketRecyclerView.findContainingViewHolder(v);

                tourMarketOnItemClickListener.onItemClicked(tourMarketRecyclerView, holder.getBindingAdapterPosition(), v);

            }
        }
    };

    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (tourMarketOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = tourMarketRecyclerView.getChildViewHolder(v);
                return tourMarketOnItemLongClickListener.onItemLongClicked(tourMarketRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };

    private RecyclerView.OnChildAttachStateChangeListener mAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (tourMarketOnItemClickListener != null) {

                // Set click listener for entire container
                view.setOnClickListener(tourMarketOnClickListener);

                // Set click listener for each component inside each container
                for (View subView : view.getTouchables()) {
                    subView.setOnClickListener(tourMarketOnClickListener);
                }

            }
            if (tourMarketOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    private ItemClickSupport(RecyclerView recyclerView, int itemID) {
        tourMarketRecyclerView = recyclerView;
        tourMarketItemID = itemID;
        tourMarketRecyclerView.setTag(itemID, this);
        tourMarketRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickSupport addTo(RecyclerView view, int itemID) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(itemID);

        if (support == null) {
            support = new ItemClickSupport(view, itemID);
        }

        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view, int itemID) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(itemID);

        if (support != null) {
            support.detach(view);
        }

        return support;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        tourMarketOnItemClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        tourMarketOnItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(tourMarketItemID, null);
    }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }
}
