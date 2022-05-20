package com.travelappproject.model.hotel;

import java.io.IOException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.List;

public class Hotel implements Serializable {
    private long id;
    private long price;
    private String name;
    private String nameVi;
    private String fullAddress;
    private double starRate;
    private Location location;
    private String fullAddressEn;
    private long countReviews;
    private String thumbImage;
    private String desTitle;
    private String desContent;
    private String phone;
    private String checkInTime;
    private String checkOutTime;
    private HotelPolicies hotelPolicies;
    private long totalRooms;
    private List<Facility> facilities;
    private List<Image> images;
    private String provinceName;
    private String cityName;

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long value) {
        this.id = value;
    }

    @JsonProperty("price")
    public long getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(long value) {
        this.price = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("name_vi")
    public String getNameVi() {
        return nameVi;
    }

    @JsonProperty("name_vi")
    public void setNameVi(String value) {
        this.nameVi = value;
    }

    @JsonProperty("full_address")
    public String getFullAddress() {
        return fullAddress;
    }

    @JsonProperty("full_address")
    public void setFullAddress(String value) {
        this.fullAddress = value;
    }

    @JsonProperty("star_rate")
    public double getStarRate() {
        return starRate;
    }

    @JsonProperty("star_rate")
    public void setStarRate(double value) {
        this.starRate = value;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location value) {
        this.location = value;
    }

    @JsonProperty("full_address_en")
    public String getFullAddressEn() {
        return fullAddressEn;
    }

    @JsonProperty("full_address_en")
    public void setFullAddressEn(String value) {
        this.fullAddressEn = value;
    }

    @JsonProperty("count_reviews")
    public long getCountReviews() {
        return countReviews;
    }

    @JsonProperty("count_reviews")
    public void setCountReviews(long value) {
        this.countReviews = value;
    }

    @JsonProperty("thumb_image")
    public String getThumbImage() {
        return thumbImage;
    }

    @JsonProperty("thumb_image")
    public void setThumbImage(String value) {
        this.thumbImage = value;
    }

    @JsonProperty("des_title")
    public String getDesTitle() {
        return desTitle;
    }

    @JsonProperty("des_title")
    public void setDesTitle(String value) {
        this.desTitle = value;
    }

    @JsonProperty("des_content")
    public String getDesContent() {
        return desContent;
    }

    @JsonProperty("des_content")
    public void setDesContent(String value) {
        this.desContent = value;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String value) {
        this.phone = value;
    }

    @JsonProperty("check_in_time")
    @JsonFormat(pattern = "HH:mm:ssX", timezone = "UTC")
    public String getCheckInTime() {
        return checkInTime;
    }

    @JsonProperty("check_in_time")
    @JsonFormat(pattern = "HH:mm:ssX", timezone = "UTC")
    public void setCheckInTime(String value) {
        this.checkInTime = value;
    }

    @JsonProperty("check_out_time")
    @JsonFormat(pattern = "HH:mm:ssX", timezone = "UTC")
    public String getCheckOutTime() {
        return checkOutTime;
    }

    @JsonProperty("check_out_time")
    @JsonFormat(pattern = "HH:mm:ssX", timezone = "UTC")
    public void setCheckOutTime(String value) {
        this.checkOutTime = value;
    }

    @JsonProperty("hotel_policies")
    public HotelPolicies getHotelPolicies() {
        return hotelPolicies;
    }

    @JsonProperty("hotel_policies")
    public void setHotelPolicies(HotelPolicies value) {
        this.hotelPolicies = value;
    }

    @JsonProperty("total_rooms")
    public long getTotalRooms() {
        return totalRooms;
    }

    @JsonProperty("total_rooms")
    public void setTotalRooms(long value) {
        this.totalRooms = value;
    }

    @JsonProperty("facilities")
    public List<Facility> getFacilities() {
        return facilities;
    }

    @JsonProperty("facilities")
    public void setFacilities(List<Facility> value) {
        this.facilities = value;
    }

    @JsonProperty("images")
    public List<Image> getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(List<Image> value) {
        this.images = value;
    }

    @JsonProperty("province_name")
    public String getProvinceName() {
        return provinceName;
    }

    @JsonProperty("province_name")
    public void setProvinceName(String value) {
        this.provinceName = value;
    }

    @JsonProperty("city_name")
    public String getCityName() {
        return cityName;
    }

    @JsonProperty("city_name")
    public void setCityName(String value) {
        this.cityName = value;
    }
}


