package com.tourtrek.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Attraction {

    private List<String> reviews;
    //private Location extendedLocation;
    private String location;
    private int cost;
    private String name;
    private String description;
    private String attractionUID;

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
    public Attraction(String name, String description, int cost, String location, String UID){
//        this.reviews = new ArrayList<String>();
//        this.extendedLocation = new Location();
//        this.cost = 0;
        this.location = location;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.attractionUID = UID;
    }

    /**
     * Get attraction cost
     *
     * @return cost of this attraction
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Set attraction cost
     *
     * @param cost
     */
    public void setCost(int cost) {
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

//    public Location getExtendedLocation() {
//        if (extendedLocation != null){
//            return extendedLocation;
//        }
//        return new Location();
//    }
//
//    public void setExtendedLocation(Location extendedLocation) {
//        this.extendedLocation = extendedLocation;
//    }

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

    public String getLocation() {
        if (this.location != null){
            return this.location;
        }
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
