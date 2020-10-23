package com.tourtrek.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Attraction {

    private List<String> reviews;
    private Location extendedLocation;
    private float cost;
    private String name;
    private String description;
    private String attractionUID; // not user-bound; universal unique ID

    /**
     * Attraction constructor, default
     */
    public Attraction(){
//        this.reviews = new ArrayList<String>();
//        this.extendedLocation = new Location();
//        this.cost = 0;
//        this.name = "";
//        this.description = "";
    }

    /**
     * Attraction constructor, detailed
     */
    public Attraction(String name, String description){
//        this.reviews = new ArrayList<String>();
//        this.extendedLocation = new Location();
//        this.cost = 0;
        this.name = name;
        this.description = description;
    }

    /**
     * Get attraction cost
     *
     * @return cost of this attraction
     */
    public float getCost() {
        return this.cost;
    }

    /**
     * Set attraction cost
     *
     * @param cost
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * Getter for reviews
     *
     * @return current reviews
     */
    public List<String> getReviews() {
        if (this.reviews == null){
            return new ArrayList<String>();
        }
        return this.reviews;
    }

    /**
     * Setter for reviews
     *
     * @param reviews list of reviews for the tour
     */
    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public Location getExtendedLocation() {
        if (extendedLocation != null){
            return extendedLocation;
        }
        return new Location();
    }

    public void setExtendedLocation(Location extendedLocation) {
        this.extendedLocation = extendedLocation;
    }

    public String getName() {
        if (this.name != null){
            return this.name;
        }
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (this.description != null){
            return this.description;
        }
        return "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttractionUID() {
        if (this.attractionUID != null){
            return attractionUID;
        }
        return UUID.randomUUID().toString();
    }

    public void setAttractionUID(String attractionUID) {
        this.attractionUID = attractionUID;
    }
}
