package com.tourtrek.utilities;

import com.tourtrek.data.Attraction;

import java.util.Comparator;

public class AttractionCostSorter implements Comparator<Attraction> {
    @Override
    public int compare(Attraction o1, Attraction o2) {
        if (o1.getCost() > o2.getCost())
            return 1;
        return -1;
    }
}
