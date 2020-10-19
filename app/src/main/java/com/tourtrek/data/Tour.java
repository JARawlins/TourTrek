package com.tourtrek.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Tour {

    private String name;
    private Boolean publiclyAvailable;
    private Boolean notifications;
    private String coverImageURI;
    private List<DocumentReference> attractions;
    private Timestamp startDate;
    private String location;
    private Long length;

    /**
     * Empty constructor needed for firestore
     */
    public Tour() {}

    /**
     * Getter for name
     *
     * @return current name
     */
    public String getName() {
        return name;
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

    /**
     * Getter for coverImageURI
     *
     * @return current coverImageURI
     */
    public String getCoverImageURI() {
        return coverImageURI;
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
        return attractions;
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
        return startDate;
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
        return location;
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
}
