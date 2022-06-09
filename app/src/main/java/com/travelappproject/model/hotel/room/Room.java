
package com.travelappproject.model.hotel.room;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Room implements Serializable {
    private String idHotel;
    private long number;
    private boolean available = true;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(String idHotel) {
        this.idHotel = idHotel;
    }

    @SerializedName("bed_types")
    private List<BedType> mBedTypes;
    @SerializedName("cancel_policies")
    private String mCancelPolicies;
    @SerializedName("facilities")
    private String mFacilities;
    @SerializedName("id")
    private String mId;
    @SerializedName("meal_plan")
    private String mMealPlan;
    @SerializedName("name")
    private String mName;
    @SerializedName("photos")
    private List<Photo> mPhotos;
    @SerializedName("price")
    private Long mPrice;
    @SerializedName("room_area")
    private String mRoomArea;
    @SerializedName("thumb")
    private Thumb mThumb;

    public List<BedType> getBedTypes() {
        return mBedTypes;
    }

    public void setBedTypes(List<BedType> bedTypes) {
        mBedTypes = bedTypes;
    }

    public String getCancelPolicies() {
        return mCancelPolicies;
    }

    public void setCancelPolicies(String cancelPolicies) {
        mCancelPolicies = cancelPolicies;
    }

    public String getFacilities() {
        return mFacilities;
    }

    public void setFacilities(String facilities) {
        mFacilities = facilities;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMealPlan() {
        return mMealPlan;
    }

    public void setMealPlan(String mealPlan) {
        mMealPlan = mealPlan;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public Long getPrice() {
        return mPrice;
    }

    public void setPrice(Long price) {
        mPrice = price;
    }

    public String getRoomArea() {
        return mRoomArea;
    }

    public void setRoomArea(String roomArea) {
        mRoomArea = roomArea;
    }

    public Thumb getThumb() {
        return mThumb;
    }

    public void setThumb(Thumb thumb) {
        mThumb = thumb;
    }

}
