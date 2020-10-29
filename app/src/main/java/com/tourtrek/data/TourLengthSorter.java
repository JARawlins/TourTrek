package com.tourtrek.data;

import java.util.Comparator;

public class TourLengthSorter implements Comparator<Tour> {
    @Override
    public int compare(Tour o1, Tour o2) {
        if (o2.getLength() > o1.getLength())
            return 1;
        else return -1;
    }
}
