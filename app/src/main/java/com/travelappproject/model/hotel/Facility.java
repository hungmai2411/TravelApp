package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class Facility implements Serializable {
    private long id;
    private String name;
    private String nameVi;
    private long categoryId;
    private String code;
    private String type;
    private Date createdAt;

    public Facility() {
    }

    public Facility(long id, String name, String nameVi, long categoryId, String code, String type, Date createdAt) {
        this.id = id;
        this.name = name;
        this.nameVi = nameVi;
        this.categoryId = categoryId;
        this.code = code;
        this.type = type;
        this.createdAt = createdAt;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long value) {
        this.id = value;
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

    @JsonProperty("category_id")
    public long getCategoryId() {
        return categoryId;
    }

    @JsonProperty("category_id")
    public void setCategoryId(long value) {
        this.categoryId = value;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String value) {
        this.code = value;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String value) {
        this.type = value;
    }

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX", timezone = "UTC")
    public void setCreatedAt(Date value) {
        this.createdAt = value;
    }
}
