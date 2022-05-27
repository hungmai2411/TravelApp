package com.travelappproject.model.hotel;

public class User {
    private String name;
    private String address;
    private String about;
    private String imageURL;
    private String phoneNumber;

    public User(){}

    public User(String name, String address, String about, String imageURL, String phoneNumber) {
        this.name = name;
        this.address = address;
        this.about = about;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
