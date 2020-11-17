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

        //Create 3 tours and set fields
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

        //Expected result of size 1
        List<Tour> result = tourMarketFragment.findTours(tours, "tour1");
        assertEquals(result.get(0).getName(), "tour1");
        assertEquals(result.size(), 1);
    }

    @Test
    public void findToursNoResult_test(){

        //Create 3 tours and set fields
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

        //Expected result of size 0
        List<Tour> result = tourMarketFragment.findTours(tours, "tour9");
        assertEquals(result.size(), 0);
    }

    @Test
    public void sortToursSortBy_test(){

        //Create 3 tours and set fields
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

        //Expected size 3
        List<Tour> result = tourMarketFragment.sortedTours(tours, "SortBy");
        assertEquals(result.size(), 3);
    }

    @Test
    public void sortToursByNameAscending_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour0", "tour1", "tour2"
        List<Tour> result = tourMarketFragment.sortedTours(tours, "Name Ascending");
        assertEquals(result.get(0).getName(), "tour0");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour2");
        assertEquals(result.size(), 3);

    }

    @Test
    public void sortToursByLocationAscending_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour0", "tour1", "tour2"
        List<Tour> result = tourMarketFragment.sortedTours(tours, "Location Ascending");
        assertEquals(result.get(0).getName(), "tour0");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour2");
        assertEquals(result.size(), 3);
    }

    @Test
    public void sortToursByDurationAscending_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour0", "tour1", "tour2"
        List<Tour> result = tourMarketFragment.sortedTours(tours, "Duration Ascending");
        assertEquals(result.get(0).getName(), "tour0");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour2");
        assertEquals(result.size(), 3);
    }

    @Test
    public void sortToursByNameDescending_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour2", "tour1", "tour0"
        List<Tour> result = tourMarketFragment.sortedTours(tours, "Name Descending");
        assertEquals(result.get(0).getName(), "tour2");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour0");
        assertEquals(result.size(), 3);
    }

    @Test
    public void sortToursByLocationDescending_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour2", "tour1", "tour0"
        List<Tour> result = tourMarketFragment.sortedTours(tours, "Location Descending");
        assertEquals(result.get(0).getName(), "tour2");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour0");
        assertEquals(result.size(), 3);
    }

    @Test
    public void sortToursByDurationDescending_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour2", "tour1", "tour0"
        List<Tour> result = tourMarketFragment.sortedTours(tours, "Duration Descending");
        assertEquals(result.get(0).getName(), "tour2");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour0");
        assertEquals(result.size(), 3);
    }

    @Test
    public void searchThenSort_test(){

        //Create 3 tours and set fields
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

        //Expected order "tour2", "tour1", "tour0". 3 expected
        List<Tour> searchResult = tourMarketFragment.findTours(tours, "to");
        List<Tour> result = tourMarketFragment.sortedTours(searchResult, "Name Descending");
        assertEquals(result.get(0).getName(), "tour2");
        assertEquals(result.get(1).getName(), "tour1");
        assertEquals(result.get(2).getName(), "tour0");
        assertEquals(result.size(), 3);
    }

    @Test
    public void searchThenSortToursNoResult_test(){

        //Create 3 tours and set fields
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

        //Expected size 0
        List<Tour> searchResult = tourMarketFragment.findTours(tours, "tour5");
        List<Tour> result = tourMarketFragment.sortedTours(searchResult, "Name Descending");
        assertEquals(result.size(), 0);
    }
}