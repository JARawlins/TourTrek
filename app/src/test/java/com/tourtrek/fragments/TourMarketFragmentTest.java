package com.tourtrek.fragments;

import com.tourtrek.data.Tour;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TourMarketFragmentTest {
    TourMarketFragment tourMarketFragment;

    @Before
    public void setup() {
        tourMarketFragment = new TourMarketFragment();
    }

    @Test
    public void findTours_test(){

        //Create 3 tours and set there names
        Tour tour0 = new Tour();
        Tour tour1 = new Tour();
        Tour tour2 = new Tour();
        tour0.setName("tour0");
        tour0.setLocation("location_tour0");
        tour0.setLength((long) 0);

        tour1.setName("tour1");
        tour1.setLocation("location_tour1");
        tour1.setLength((long) 1);

        tour2.setName("tour2");
        tour2.setLocation("location_tour2");
        tour2.setLength((long) 2);

        //Add the tours a list
        List<Tour> tours = new ArrayList<>();
        tours.add(tour2);
        tours.add(tour0);
        tours.add(tour1);

        List<Tour> result = tourMarketFragment.findTours(tours, "tour1");
        assertEquals(result.get(0).getName(), "tour1");

    }
}