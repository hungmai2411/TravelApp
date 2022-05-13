package com.travelappproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Hotel implements Serializable {
    private long sn;
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private long districtSn;
    private String districtName;
    private long provinceSn;
    private String provinceName;
    private Object description;
    private Object enDescription;
    private boolean newHotel;
    private boolean hotHotel;
    private long hotelStatus;
    private long totalReview;
    private long totalFavorite;
    private boolean roomAvailable;
    private double averageMark;
    private String imagePath;
    private double distance;
    private long firstHours;
    private boolean favorite;
    private boolean paymentPromotion;
    private boolean hourly;
    private boolean overnight;
    private boolean daily;
    private long numToRedeem;
    private long cancelBeforeHours;
    private List<Object> flashSaleList;
    private List<HotelImageList> hotelImageList;
    private List<Object> roomTypeList;
    private List<FacilityFormList> facilityFormList;
    private List<RoomSettingFormList> roomSettingFormList;
    private List<Object> hotelProductTypeFormList;
    private long checkin;
    private long checkout;
    private long startOvernight;
    private long endOvernight;
    private long startHourlyTime;
    private long origin;
    private String type;
    private double starRating;
    private List<Object> nearbyList;
    private List<Object> languageSpokenList;
    private HotelPolicy hotelPolicy;
    private long paymentWay;
    private List<Object> hotelProductList;

    @JsonProperty("sn")
    public long getSn() {
        return sn;
    }

    @JsonProperty("sn")
    public void setSn(long value) {
        this.sn = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String value) {
        this.address = value;
    }

    @JsonProperty("longitude")
    public double getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(double value) {
        this.longitude = value;
    }

    @JsonProperty("latitude")
    public double getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(double value) {
        this.latitude = value;
    }

    @JsonProperty("districtSn")
    public long getDistrictSn() {
        return districtSn;
    }

    @JsonProperty("districtSn")
    public void setDistrictSn(long value) {
        this.districtSn = value;
    }

    @JsonProperty("districtName")
    public String getDistrictName() {
        return districtName;
    }

    @JsonProperty("districtName")
    public void setDistrictName(String value) {
        this.districtName = value;
    }

    @JsonProperty("provinceSn")
    public long getProvinceSn() {
        return provinceSn;
    }

    @JsonProperty("provinceSn")
    public void setProvinceSn(long value) {
        this.provinceSn = value;
    }

    @JsonProperty("provinceName")
    public String getProvinceName() {
        return provinceName;
    }

    @JsonProperty("provinceName")
    public void setProvinceName(String value) {
        this.provinceName = value;
    }

    @JsonProperty("description")
    public Object getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(Object value) {
        this.description = value;
    }

    @JsonProperty("enDescription")
    public Object getEnDescription() {
        return enDescription;
    }

    @JsonProperty("enDescription")
    public void setEnDescription(Object value) {
        this.enDescription = value;
    }

    @JsonProperty("newHotel")
    public boolean getNewHotel() {
        return newHotel;
    }

    @JsonProperty("newHotel")
    public void setNewHotel(boolean value) {
        this.newHotel = value;
    }

    @JsonProperty("hotHotel")
    public boolean getHotHotel() {
        return hotHotel;
    }

    @JsonProperty("hotHotel")
    public void setHotHotel(boolean value) {
        this.hotHotel = value;
    }

    @JsonProperty("hotelStatus")
    public long getHotelStatus() {
        return hotelStatus;
    }

    @JsonProperty("hotelStatus")
    public void setHotelStatus(long value) {
        this.hotelStatus = value;
    }

    @JsonProperty("totalReview")
    public long getTotalReview() {
        return totalReview;
    }

    @JsonProperty("totalReview")
    public void setTotalReview(long value) {
        this.totalReview = value;
    }

    @JsonProperty("totalFavorite")
    public long getTotalFavorite() {
        return totalFavorite;
    }

    @JsonProperty("totalFavorite")
    public void setTotalFavorite(long value) {
        this.totalFavorite = value;
    }

    @JsonProperty("roomAvailable")
    public boolean getRoomAvailable() {
        return roomAvailable;
    }

    @JsonProperty("roomAvailable")
    public void setRoomAvailable(boolean value) {
        this.roomAvailable = value;
    }

    @JsonProperty("averageMark")
    public double getAverageMark() {
        return averageMark;
    }

    @JsonProperty("averageMark")
    public void setAverageMark(double value) {
        this.averageMark = value;
    }

    @JsonProperty("imagePath")
    public String getImagePath() {
        return imagePath;
    }

    @JsonProperty("imagePath")
    public void setImagePath(String value) {
        this.imagePath = value;
    }

    @JsonProperty("distance")
    public double getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(double value) {
        this.distance = value;
    }

    @JsonProperty("firstHours")
    public long getFirstHours() {
        return firstHours;
    }

    @JsonProperty("firstHours")
    public void setFirstHours(long value) {
        this.firstHours = value;
    }

    @JsonProperty("favorite")
    public boolean getFavorite() {
        return favorite;
    }

    @JsonProperty("favorite")
    public void setFavorite(boolean value) {
        this.favorite = value;
    }

    @JsonProperty("paymentPromotion")
    public boolean getPaymentPromotion() {
        return paymentPromotion;
    }

    @JsonProperty("paymentPromotion")
    public void setPaymentPromotion(boolean value) {
        this.paymentPromotion = value;
    }

    @JsonProperty("hourly")
    public boolean getHourly() {
        return hourly;
    }

    @JsonProperty("hourly")
    public void setHourly(boolean value) {
        this.hourly = value;
    }

    @JsonProperty("overnight")
    public boolean getOvernight() {
        return overnight;
    }

    @JsonProperty("overnight")
    public void setOvernight(boolean value) {
        this.overnight = value;
    }

    @JsonProperty("daily")
    public boolean getDaily() {
        return daily;
    }

    @JsonProperty("daily")
    public void setDaily(boolean value) {
        this.daily = value;
    }

    @JsonProperty("numToRedeem")
    public long getNumToRedeem() {
        return numToRedeem;
    }

    @JsonProperty("numToRedeem")
    public void setNumToRedeem(long value) {
        this.numToRedeem = value;
    }

    @JsonProperty("cancelBeforeHours")
    public long getCancelBeforeHours() {
        return cancelBeforeHours;
    }

    @JsonProperty("cancelBeforeHours")
    public void setCancelBeforeHours(long value) {
        this.cancelBeforeHours = value;
    }

    @JsonProperty("flashSaleList")
    public List<Object> getFlashSaleList() {
        return flashSaleList;
    }

    @JsonProperty("flashSaleList")
    public void setFlashSaleList(List<Object> value) {
        this.flashSaleList = value;
    }

    @JsonProperty("hotelImageList")
    public List<HotelImageList> getHotelImageList() {
        return hotelImageList;
    }

    @JsonProperty("hotelImageList")
    public void setHotelImageList(List<HotelImageList> value) {
        this.hotelImageList = value;
    }

    @JsonProperty("roomTypeList")
    public List<Object> getRoomTypeList() {
        return roomTypeList;
    }

    @JsonProperty("roomTypeList")
    public void setRoomTypeList(List<Object> value) {
        this.roomTypeList = value;
    }

    @JsonProperty("facilityFormList")
    public List<FacilityFormList> getFacilityFormList() {
        return facilityFormList;
    }

    @JsonProperty("facilityFormList")
    public void setFacilityFormList(List<FacilityFormList> value) {
        this.facilityFormList = value;
    }

    @JsonProperty("roomSettingFormList")
    public List<RoomSettingFormList> getRoomSettingFormList() {
        return roomSettingFormList;
    }

    @JsonProperty("roomSettingFormList")
    public void setRoomSettingFormList(List<RoomSettingFormList> value) {
        this.roomSettingFormList = value;
    }

    @JsonProperty("hotelProductTypeFormList")
    public List<Object> getHotelProductTypeFormList() {
        return hotelProductTypeFormList;
    }

    @JsonProperty("hotelProductTypeFormList")
    public void setHotelProductTypeFormList(List<Object> value) {
        this.hotelProductTypeFormList = value;
    }

    @JsonProperty("checkin")
    public long getCheckin() {
        return checkin;
    }

    @JsonProperty("checkin")
    public void setCheckin(long value) {
        this.checkin = value;
    }

    @JsonProperty("checkout")
    public long getCheckout() {
        return checkout;
    }

    @JsonProperty("checkout")
    public void setCheckout(long value) {
        this.checkout = value;
    }

    @JsonProperty("startOvernight")
    public long getStartOvernight() {
        return startOvernight;
    }

    @JsonProperty("startOvernight")
    public void setStartOvernight(long value) {
        this.startOvernight = value;
    }

    @JsonProperty("endOvernight")
    public long getEndOvernight() {
        return endOvernight;
    }

    @JsonProperty("endOvernight")
    public void setEndOvernight(long value) {
        this.endOvernight = value;
    }

    @JsonProperty("startHourlyTime")
    public long getStartHourlyTime() {
        return startHourlyTime;
    }

    @JsonProperty("startHourlyTime")
    public void setStartHourlyTime(long value) {
        this.startHourlyTime = value;
    }

    @JsonProperty("origin")
    public long getOrigin() {
        return origin;
    }

    @JsonProperty("origin")
    public void setOrigin(long value) {
        this.origin = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }

    @JsonProperty("starRating")
    public double getStarRating() {
        return starRating;
    }

    @JsonProperty("starRating")
    public void setStarRating(double value) {
        this.starRating = value;
    }

    @JsonProperty("nearbyList")
    public List<Object> getNearbyList() {
        return nearbyList;
    }

    @JsonProperty("nearbyList")
    public void setNearbyList(List<Object> value) {
        this.nearbyList = value;
    }

    @JsonProperty("languageSpokenList")
    public List<Object> getLanguageSpokenList() {
        return languageSpokenList;
    }

    @JsonProperty("languageSpokenList")
    public void setLanguageSpokenList(List<Object> value) {
        this.languageSpokenList = value;
    }

    @JsonProperty("hotelPolicy")
    public HotelPolicy getHotelPolicy() {
        return hotelPolicy;
    }

    @JsonProperty("hotelPolicy")
    public void setHotelPolicy(HotelPolicy value) {
        this.hotelPolicy = value;
    }

    @JsonProperty("paymentWay")
    public long getPaymentWay() {
        return paymentWay;
    }

    @JsonProperty("paymentWay")
    public void setPaymentWay(long value) {
        this.paymentWay = value;
    }

    @JsonProperty("hotelProductList")
    public List<Object> getHotelProductList() {
        return hotelProductList;
    }

    @JsonProperty("hotelProductList")
    public void setHotelProductList(List<Object> value) {
        this.hotelProductList = value;
    }
}
