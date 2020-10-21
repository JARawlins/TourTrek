package com.tourtrek.data;

import java.util.ArrayList;
import java.util.List;

public interface AttractionADT {
    /**
     * Get attraction cost
     *
     * @return cost of this attraction
     */
    public float getCost();

    /**
     * Set attraction cost
     *
     * @param cost
     */
    public void setCost(float cost);

    /**
     * Getter for reviews
     *
     * @return current reviews
     */
    public List<String> getReviews();

    /**
     * Setter for reviews
     *
     * @param reviews list of reviews for the tour
     */
    public void setReviews(List<String> reviews);

    public Location getExtendedLocation();

    public void setExtendedLocation(Location extendedLocation);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

}
