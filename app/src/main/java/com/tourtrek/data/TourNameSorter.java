package com.tourtrek.data;

import java.util.Comparator;

public class TourNameSorter implements Comparator <Tour> {

    @Override
    public int compare(Tour o1, Tour o2) {
        int temp = o1.getName().compareToIgnoreCase(o2.getName());
        if (temp == 0) return 1;
        return temp;
    }
}
