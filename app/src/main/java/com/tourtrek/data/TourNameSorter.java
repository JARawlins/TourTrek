package com.tourtrek.data;

import java.util.Comparator;

public class TourNameSorter implements Comparator <Tour> {

    @Override
    public int compare(Tour o1, Tour o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
