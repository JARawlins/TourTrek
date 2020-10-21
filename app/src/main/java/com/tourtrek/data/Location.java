package com.tourtrek.data;

public class Location implements LocationADT{
    private String street; // Ex: 330 N. Orchard Street
    private String unit;
    private String county;
    private String city;
    private String state;
    //private String zip;

    /**
     * Empty constructor for Firebase
     **/
    public Location(){
//        this.street = "";
//        this.unit = "";
//        this.county = "";
//        this.city = "";
//        this.state = "";
    }

    public Location(String street, String county, String state){
        this.street = street;
        this.county = county;
        this.state = state;
    }

    public String getStreet() {
        if (this.street != null){
            return this.street;
        }
        return "";
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnit() {
        if (this.unit != null){
            return this.unit;
        }
        return "";
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCounty() {
        if (this.county != null){
            return this.county;
        }
        return "";
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        if (this.city != null){
            return this.city;
        }
        return "";
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        if (this.state != null){
            return this.state;
        }
        return "";
    }

    public void setState(String state) {
        this.state = state;
    }
}
