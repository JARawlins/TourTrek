package com.tourtrek.data;

import com.google.firebase.firestore.DocumentReference;
import java.util.List;

public interface UserADT {
    /**
     * Getter for username
     *
     * @return current username
     */
    public String getUsername();

    /**
     * Setter for username
     *
     * @param username username to set
     */
    public void setUsername(String username);

    /**
     * Getter for email
     *
     * @return current email
     */
    public String getEmail();

    /**
     * Setter for email
     *
     * @param email email to set
     */
    public void setEmail(String email);

    /**
     * Getter for profileImageURI
     *
     * @return current profileImageURI
     */
    public String getProfileImageURI();

    /**
     * Setter for profileImageURI
     *
     * @param profileImageURI profileImageURI to set
     */
    public void setProfileImageURI(String profileImageURI);

    /**
     * Getter for tours
     *
     * @return current tours
     */
    public List<DocumentReference> getTours();

    /**
     * Setter for tours
     *
     * @param tours tours to set
     */
    public void setTours(List<DocumentReference> tours);

    public List<DocumentReference> getContacts();

    public void setContacts(List<DocumentReference> contacts);
}
