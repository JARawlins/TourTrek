package com.tourtrek.data;

public class User {

    private String username;
    private String email;
    private String profileImageURI;

    /**
     * Empty constructor needed for firestore
     */
    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Getter for username
     *
     * @return current username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     *
     * @param username username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for email
     *
     * @return current email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     *
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for profileImageURI
     *
     * @return current profileImageURI
     */
    public String getProfileImageURI() {
        return profileImageURI;
    }

    /**
     * Setter for profileImageURI
     *
     * @param profileImageURI profileImageURI to set
     */
    public void setProfileImageURI(String profileImageURI) {
        this.profileImageURI = profileImageURI;
    }
}
