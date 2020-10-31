package com.tourtrek.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import java.util.ArrayList;
import java.util.List;

public class Tour{
    private String name;
    private Timestamp startDate;
    private String location;
    private Long length;
    private float cost;
    private Boolean notifications;
    private List<String> reviews;
    private String description;
    private Boolean publiclyAvailable;
    private List<DocumentReference> attractions;
    private String coverImageURI;
    private String tourUID;

    /**
     * Empty constructor needed for Firestore
     */
    public Tour() {}

    /**
     * Getter for name
     *
     * @return current name
     */
    public String getName() {
        if (this.name != null){
            return this.name;
        }
        return "";
    }

    /**
     * Setter for name
     *
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for coverImageURI
     *
     * @return current coverImageURI
     */
    public String getCoverImageURI() {
        if (this.coverImageURI != null){
            this.coverImageURI = "";
        }
        return this.coverImageURI;
    }

    /**
     * Setter for coverImageURI
     *
     * @param coverImageURI coverImageURI to set
     */
    public void setCoverImageURI(String coverImageURI) {
        this.coverImageURI = coverImageURI;
    }

    /**
     * Getter for attractions
     *
     * @return current attractions
     */
    public List<DocumentReference> getAttractions() {
        if (this.attractions == null){
            this.attractions = new ArrayList<>();
        }
        return this.attractions;
    }

    /**
     * Setter for attractions
     *
     * @param attractions attractions to set
     */
    public void setAttractions(List<DocumentReference> attractions) {
        this.attractions = attractions;
    }

    public void addAttraction(DocumentReference attraction) {
        if (attractions == null) {
            attractions = new ArrayList<>();
        }
        attractions.add(attraction);
    }

    /**
     * Getter for startDate
     *
     * @return current startDate
     */
    public Timestamp getStartDate() {
        if (this.startDate != null){
            this.startDate = Timestamp.now();
        }
        return this.startDate;
    }

    /**
     * Setter for startDate
     *
     * @param startDate startDate to set
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter for notifications
     *
     * @return current notifications
     */
    public Boolean getNotifications() {
        return notifications;
    }

    /**
     * Setter for notifications
     *
     * @param notifications notifications to set
     */
    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    /**
     * Getter for location
     *
     * @return current location
     */
    public String getLocation() {
        if (this.location != null){
            this.location = "";
        }
        return this.location;
    }

    /**
     * Setter for location
     *
     * @param location location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for length
     *
     * @return current length
     */
    public Long getLength() {
        return length;
    }

    /**
     * Setter for length
     *
     * @param length length to set
     */
    public void setLength(Long length) {
        this.length = length;
    }

    public float getCost() {
        return this.cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public List<String> getReviews() {
        if (this.reviews == null){
            return new ArrayList<>();
        }
        return this.reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
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

    /**
     * Getter for publiclyAvailable
     *
     * @return current publiclyAvailable
     */
    public Boolean isPubliclyAvailable() {
        return publiclyAvailable;
    }

    /**
     * Setter for publiclyAvailable
     *
     * @param publiclyAvailable publiclyAvailable to set
     */
    public void setPubliclyAvailable(Boolean publiclyAvailable) {
        this.publiclyAvailable = publiclyAvailable;
    }

    public String getTourUID() {
        return this.tourUID;
    }

    public void setTourUID(String tourUID) {
        this.tourUID = tourUID;
    }

}
