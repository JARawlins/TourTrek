package com.tourtrek.data;

import java.util.Comparator;

public class TourLocationSorter implements Comparator<Tour> {
    @Override
    public int compare(Tour o1, Tour o2) {
        return o1.getLocation().compareToIgnoreCase(o2.getLocation());
    }
}
