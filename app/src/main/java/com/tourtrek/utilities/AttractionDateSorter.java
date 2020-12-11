package com.tourtrek.utilities;

import com.tourtrek.data.Attraction;

import java.util.Comparator;

public class AttractionDateSorter implements Comparator<Attraction> {
    @Override
    public int compare(Attraction o1, Attraction o2) {

        if (o1.getStartDate().compareTo(o2.getStartDate()) == 0) {
            if (o1.getStartTime().compareTo(o2.getStartTime()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
        return o1.getStartDate().compareTo(o2.getStartDate());
    }
}