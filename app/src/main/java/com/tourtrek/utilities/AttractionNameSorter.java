package com.tourtrek.utilities;

import com.tourtrek.data.Attraction;

import java.util.Comparator;

public class AttractionNameSorter implements Comparator<Attraction> {
    @Override
    public int compare(Attraction o1, Attraction o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
