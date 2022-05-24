
package com.travelappproject.model.hotel.room;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Thumb implements Serializable {

    @SerializedName("order")
    private Long mOrder;
    @SerializedName("room_image")
    private String mRoomImage;
    @SerializedName("thumb_image")
    private String mThumbImage;

    public Long getOrder() {
        return mOrder;
    }

    public void setOrder(Long order) {
        mOrder = order;
    }

    public String getRoomImage() {
        return mRoomImage;
    }

    public void setRoomImage(String roomImage) {
        mRoomImage = roomImage;
    }

    public String getThumbImage() {
        return mThumbImage;
    }

    public void setThumbImage(String thumbImage) {
        mThumbImage = thumbImage;
    }

}
