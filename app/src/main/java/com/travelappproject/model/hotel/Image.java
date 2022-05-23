package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Image implements Serializable {
    private long hotelId;
    private String name;
    private HotelImageType hotelImageType;
    private long order;
    private Long orderRoom;
    private boolean isVisible;
    private boolean isVisibleInAll;
    private boolean isHotelThumb;
    private long id;
    private String slug;
    private ImageType imageType;
    private String height;
    private String width;

    @JsonProperty("hotel_id")
    public long getHotelId() {
        return hotelId;
    }

    @JsonProperty("hotel_id")
    public void setHotelId(long value) {
        this.hotelId = value;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String value) {
        this.name = value;
    }

    @JsonProperty("hotel_image_type")
    public HotelImageType getHotelImageType() {
        return hotelImageType;
    }

    @JsonProperty("hotel_image_type")
    public void setHotelImageType(HotelImageType value) {
        this.hotelImageType = value;
    }

    @JsonProperty("order")
    public long getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(long value) {
        this.order = value;
    }

    @JsonProperty("order_room")
    public Long getOrderRoom() {
        return orderRoom;
    }

    @JsonProperty("order_room")
    public void setOrderRoom(Long value) {
        this.orderRoom = value;
    }

    @JsonProperty("is_visible")
    public boolean getIsVisible() {
        return isVisible;
    }

    @JsonProperty("is_visible")
    public void setIsVisible(boolean value) {
        this.isVisible = value;
    }

    @JsonProperty("is_visible_in_all")
    public boolean getIsVisibleInAll() {
        return isVisibleInAll;
    }

    @JsonProperty("is_visible_in_all")
    public void setIsVisibleInAll(boolean value) {
        this.isVisibleInAll = value;
    }

    @JsonProperty("is_hotel_thumb")
    public boolean getIsHotelThumb() {
        return isHotelThumb;
    }

    @JsonProperty("is_hotel_thumb")
    public void setIsHotelThumb(boolean value) {
        this.isHotelThumb = value;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long value) {
        this.id = value;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String value) {
        this.slug = value;
    }

    @JsonProperty("image_type")
    public ImageType getImageType() {
        return imageType;
    }

    @JsonProperty("image_type")
    public void setImageType(ImageType value) {
        this.imageType = value;
    }

    @JsonProperty("height")
    public String getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(String value) {
        this.height = value;
    }

    @JsonProperty("width")
    public String getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(String value) {
        this.width = value;
    }
}
