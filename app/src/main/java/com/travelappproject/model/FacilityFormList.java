package com.travelappproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FacilityFormList implements Serializable {
    private long sn;
    private String name;
    private String nameEn;
    private long status;
    private String imagePath;
    private String originalName;

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

    @JsonProperty("nameEn")
    public String getNameEn() {
        return nameEn;
    }

    @JsonProperty("nameEn")
    public void setNameEn(String value) {
        this.nameEn = value;
    }

    @JsonProperty("status")
    public long getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(long value) {
        this.status = value;
    }

    @JsonProperty("imagePath")
    public String getImagePath() {
        return imagePath;
    }

    @JsonProperty("imagePath")
    public void setImagePath(String value) {
        this.imagePath = value;
    }

    @JsonProperty("originalName")
    public String getOriginalName() {
        return originalName;
    }

    @JsonProperty("originalName")
    public void setOriginalName(String value) {
        this.originalName = value;
    }
}

