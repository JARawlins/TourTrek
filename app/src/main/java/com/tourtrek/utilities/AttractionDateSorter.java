package com.tourtrek.utilities;

import com.tourtrek.data.Attraction;

import java.util.Comparator;

public class AttractionDateSorter implements Comparator<Attraction> {
    @Override
    public int compare(Attraction o1, Attraction o2) {

        return o1.getStartDate().compareTo(o2.getStartDate());
    }
}
