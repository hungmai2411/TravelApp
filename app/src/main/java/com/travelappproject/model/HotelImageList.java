package com.travelappproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class HotelImageList implements Serializable {
    private long sn;
    private long firstDisplay;
    private long type;
    private String originalName;
    private String imagePath;
    private Long roomTypeSn;
    private String roomTypeName;

    @JsonProperty("sn")
    public long getSn() {
        return sn;
    }

    @JsonProperty("sn")
    public void setSn(long value) {
        this.sn = value;
    }

    @JsonProperty("firstDisplay")
    public long getFirstDisplay() {
        return firstDisplay;
    }

    @JsonProperty("firstDisplay")
    public void setFirstDisplay(long value) {
        this.firstDisplay = value;
    }

    @JsonProperty("type")
    public long getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(long value) {
        this.type = value;
    }

    @JsonProperty("originalName")
    public String getOriginalName() {
        return originalName;
    }

    @JsonProperty("originalName")
    public void setOriginalName(String value) {
        this.originalName = value;
    }

    @JsonProperty("imagePath")
    public String getImagePath() {
        return imagePath;
    }

    @JsonProperty("imagePath")
    public void setImagePath(String value) {
        this.imagePath = value;
    }

    @JsonProperty("roomTypeSn")
    public Long getRoomTypeSn() {
        return roomTypeSn;
    }

    @JsonProperty("roomTypeSn")
    public void setRoomTypeSn(Long value) {
        this.roomTypeSn = value;
    }

    @JsonProperty("roomTypeName")
    public String getRoomTypeName() {
        return roomTypeName;
    }

    @JsonProperty("roomTypeName")
    public void setRoomTypeName(String value) {
        this.roomTypeName = value;
    }
}

