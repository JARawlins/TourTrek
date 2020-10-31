package com.tourtrek.utilities;

import com.tourtrek.data.Tour;

import java.util.Comparator;

public class TourLocationSorter implements Comparator<Tour> {
    @Override
    public int compare(Tour tour1, Tour tour2) {
        int temp = tour1.getLocation().compareToIgnoreCase(tour2.getLocation());
        if (temp == 0) return 1;
        return temp;
    }
}
