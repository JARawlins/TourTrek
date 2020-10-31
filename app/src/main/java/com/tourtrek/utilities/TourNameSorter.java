package com.tourtrek.utilities;

import com.tourtrek.data.Tour;

import java.util.Comparator;

public class TourNameSorter implements Comparator <Tour> {

    @Override
    public int compare(Tour tour1, Tour tour2) {
        int temp = tour1.getName().compareToIgnoreCase(tour2.getName());
        if (temp == 0) return 1;
        return temp;
    }
}
