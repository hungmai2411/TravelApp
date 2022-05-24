
package com.travelappproject.model.hotel.room;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BedType implements Serializable {

    @SerializedName("count")
    private String mCount;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("extra")
    private Boolean mExtra;
    @SerializedName("type")
    private String mType;

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Boolean getExtra() {
        return mExtra;
    }

    public void setExtra(Boolean extra) {
        mExtra = extra;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

}
