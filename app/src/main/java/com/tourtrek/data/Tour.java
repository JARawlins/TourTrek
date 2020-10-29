package com.tourtrek.data;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private String tourUID; // not user-bound; universal unique ID

    /**
     * Empty constructor needed for Firestore
     */
    public Tour() {
        this.startDate = Timestamp.now();
//        this.name = "";
//        this.location = "";
//        this.notifications = false;
//        this.reviews = new ArrayList<>();
//        this.description = "";
//        this.publiclyAvailable = false;
//        this.attractions = new ArrayList<>();
//        this.coverImageURI = "";
    }

    /**
     * Alternate constructor
     * To get the String UID, create a new document. Apply the getID method to its document reference.
     */
    public Tour(String name, Boolean publiclyAvailable, String description){
        this.startDate = Timestamp.now();
        this.name = name;
        this.description = description;
        this.publiclyAvailable = publiclyAvailable;
//        this.location = "";
//        this.notifications = false;
//        this.reviews = new ArrayList<>();
//        this.attractions = new ArrayList<>();
//        this.coverImageURI = "";
    }

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
            return this.coverImageURI;
        }
        return "";
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
        if (this.attractions != null){
            return this.attractions;
        }
        return new ArrayList<>();
    }

    /**
     * Setter for attractions
     *
     * @param attractions attractions to set
     */
    public void setAttractions(List<DocumentReference> attractions) {
        this.attractions = attractions;
    }

    /**
     * Getter for startDate
     *
     * @return current startDate
     */
    public Timestamp getStartDate() {
        if (this.startDate != null){
            return this.startDate;
        }
        return Timestamp.now();
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
            return this.location;
        }
        return "";
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
        if (this.description != null){
            return this.description;
        }
        return "";
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
