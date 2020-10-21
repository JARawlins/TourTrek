package com.tourtrek.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public interface TourADT {
    /**
     * Getter for name
     *
     * @return current name
     */
    public String getName();

    /**
     * Setter for name
     *
     * @param name name to set
     */
    public void setName(String name);

    /**
     * Getter for publiclyAvailable
     *
     * @return current publiclyAvailable
     */
    public Boolean isPubliclyAvailable();

    /**
     * Setter for publiclyAvailable
     *
     * @param publiclyAvailable publiclyAvailable to set
     */
    public void setPubliclyAvailable(Boolean publiclyAvailable);

    /**
     * Getter for coverImageURI
     *
     * @return current coverImageURI
     */
    public String getCoverImageURI();

    /**
     * Setter for coverImageURI
     *
     * @param coverImageURI coverImageURI to set
     */
    public void setCoverImageURI(String coverImageURI);

    /**
     * Getter for attractions
     *
     * @return current attractions
     */
    public List<DocumentReference> getAttractions();

    /**
     * Setter for attractions
     *
     * @param attractions attractions to set
     */
    public void setAttractions(List<DocumentReference> attractions);

    /**
     * Getter for startDate
     *
     * @return current startDate
     */
    public Timestamp getStartDate();

    /**
     * Setter for startDate
     *
     * @param startDate startDate to set
     */
    public void setStartDate(Timestamp startDate);

    /**
     * Getter for notifications
     *
     * @return current notifications
     */
    public Boolean getNotifications();

    /**
     * Setter for notifications
     *
     * @param notifications notifications to set
     */
    public void setNotifications(Boolean notifications);

    /**
     * Getter for location
     *
     * @return current location
     */
    public String getLocation();

    /**
     * Setter for location
     *
     * @param location location to set
     */
    public void setLocation(String location);

    /**
     * Getter for length
     *
     * @return current length
     */
    public Long getLength();

    /**
     * Setter for length
     *
     * @param length length to set
     */
    public void setLength(Long length);

    public float getCost();

    public void setCost(float cost);

    public List<String> getReviews();

    public void setReviews(List<String> reviews);

    public String getDescription();

    public void setDescription(String description);

}
