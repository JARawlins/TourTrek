package com.tourtrek.data;

import java.util.Comparator;

public class TourLocationSorter implements Comparator<Tour> {
    @Override
    public int compare(Tour o1, Tour o2) {
        int temp = o1.getLocation().compareToIgnoreCase(o2.getLocation());
        if (temp == 0) return 1;
        return temp;
    }
}
