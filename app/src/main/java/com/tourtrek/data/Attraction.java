package com.tourtrek.data;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Attraction {

    private List<String> reviews;
    private String location;
    private float cost;
    private String name;
    private String description;
    private String attractionUID;
    private Timestamp startDate;
    private String startTime;
    private Timestamp endDate;
    private String endTime;
    private String coverImageURI;

    /**
     * Empty constructor needed for Firestore
     */
    public Attraction(){}


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCoverImageURI() {
        return coverImageURI;
    }

    public void setCoverImageURI(String coverImageURI) {
        this.coverImageURI = coverImageURI;
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

    public void setStartDateFromString(String startDateStr) throws ParseException {

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");

        final Timestamp startDate = new Timestamp((Date) formatter.parse(startDateStr));

        this.startDate = startDate;
    }

    public void setEndDateFromString(String endDateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");

        this.endDate = new Timestamp(formatter.parse(endDateString));
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
