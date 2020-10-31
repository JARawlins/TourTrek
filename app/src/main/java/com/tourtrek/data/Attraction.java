package com.tourtrek.data;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Attraction {

    private List<String> reviews;
    private String location;
    private int cost;
    private String name;
    private String description;
    private String attractionUID;
    private Timestamp startDate;
    private Timestamp endDate;

    /**
     * Empty constructor needed for Firestore
     */
    public Attraction(){}

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

    public String getName() {
        if (this.name == null){
            this.name = "";
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (this.description == null){
            this.description = "";
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttractionUID() {
        return attractionUID;
    }

    public void setAttractionUID(String attractionUID) {
        this.attractionUID = attractionUID;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getStartDate() {
        if (this.startDate == null) {
            startDate = Timestamp.now();
        }
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        if (this.endDate == null) {
            endDate = Timestamp.now();
        }
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
}
