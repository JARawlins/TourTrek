package com.tourtrek.data;

import java.util.Comparator;

public class TourLengthSorter implements Comparator<Tour> {
    @Override
    public int compare(Tour tour1, Tour tour2) {
        if (tour2.getLength() > tour1.getLength())
            return -1;
        else return 1;
    }
}
