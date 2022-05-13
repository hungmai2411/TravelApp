package com.travelappproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class RoomSettingFormList implements Serializable {
    private long sn;
    private long roomTypeSn;
    private String roomTypeName;
    private long hotelSn;
    private String hotelName;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    private boolean wHourly;
    private boolean wOvernight;
    private boolean wDaily;
    private String wStartTime;
    private String wEndTime;
    private boolean sHourly;
    private boolean sOvernight;
    private boolean sDaily;
    private String sStartTime;
    private String sEndTime;
    private boolean notConfirmed;
    private Date startDate;
    private Date endDate;
    private long applyFlashSale;
    private boolean lockWeenkend;
    private boolean lockSpecial;

    @JsonProperty("sn")
    public long getSn() {
        return sn;
    }

    @JsonProperty("sn")
    public void setSn(long value) {
        this.sn = value;
    }

    @JsonProperty("roomTypeSn")
    public long getRoomTypeSn() {
        return roomTypeSn;
    }

    @JsonProperty("roomTypeSn")
    public void setRoomTypeSn(long value) {
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

    @JsonProperty("hotelSn")
    public long getHotelSn() {
        return hotelSn;
    }

    @JsonProperty("hotelSn")
    public void setHotelSn(long value) {
        this.hotelSn = value;
    }

    @JsonProperty("hotelName")
    public String getHotelName() {
        return hotelName;
    }

    @JsonProperty("hotelName")
    public void setHotelName(String value) {
        this.hotelName = value;
    }

    @JsonProperty("friday")
    public boolean getFriday() {
        return friday;
    }

    @JsonProperty("friday")
    public void setFriday(boolean value) {
        this.friday = value;
    }

    @JsonProperty("saturday")
    public boolean getSaturday() {
        return saturday;
    }

    @JsonProperty("saturday")
    public void setSaturday(boolean value) {
        this.saturday = value;
    }

    @JsonProperty("sunday")
    public boolean getSunday() {
        return sunday;
    }

    @JsonProperty("sunday")
    public void setSunday(boolean value) {
        this.sunday = value;
    }

    @JsonProperty("wHourly")
    public boolean getWHourly() {
        return wHourly;
    }

    @JsonProperty("wHourly")
    public void setWHourly(boolean value) {
        this.wHourly = value;
    }

    @JsonProperty("wOvernight")
    public boolean getWOvernight() {
        return wOvernight;
    }

    @JsonProperty("wOvernight")
    public void setWOvernight(boolean value) {
        this.wOvernight = value;
    }

    @JsonProperty("wDaily")
    public boolean getWDaily() {
        return wDaily;
    }

    @JsonProperty("wDaily")
    public void setWDaily(boolean value) {
        this.wDaily = value;
    }

    @JsonProperty("wStartTime")
    public String getWStartTime() {
        return wStartTime;
    }

    @JsonProperty("wStartTime")
    public void setWStartTime(String value) {
        this.wStartTime = value;
    }

    @JsonProperty("wEndTime")
    public String getWEndTime() {
        return wEndTime;
    }

    @JsonProperty("wEndTime")
    public void setWEndTime(String value) {
        this.wEndTime = value;
    }

    @JsonProperty("sHourly")
    public boolean getSHourly() {
        return sHourly;
    }

    @JsonProperty("sHourly")
    public void setSHourly(boolean value) {
        this.sHourly = value;
    }

    @JsonProperty("sOvernight")
    public boolean getSOvernight() {
        return sOvernight;
    }

    @JsonProperty("sOvernight")
    public void setSOvernight(boolean value) {
        this.sOvernight = value;
    }

    @JsonProperty("sDaily")
    public boolean getSDaily() {
        return sDaily;
    }

    @JsonProperty("sDaily")
    public void setSDaily(boolean value) {
        this.sDaily = value;
    }

    @JsonProperty("sStartTime")
    public String getSStartTime() {
        return sStartTime;
    }

    @JsonProperty("sStartTime")
    public void setSStartTime(String value) {
        this.sStartTime = value;
    }

    @JsonProperty("sEndTime")
    public String getSEndTime() {
        return sEndTime;
    }

    @JsonProperty("sEndTime")
    public void setSEndTime(String value) {
        this.sEndTime = value;
    }

    @JsonProperty("notConfirmed")
    public boolean getNotConfirmed() {
        return notConfirmed;
    }

    @JsonProperty("notConfirmed")
    public void setNotConfirmed(boolean value) {
        this.notConfirmed = value;
    }

    @JsonProperty("startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setStartDate(Date value) {
        this.startDate = value;
    }

    @JsonProperty("endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date value) {
        this.endDate = value;
    }

    @JsonProperty("applyFlashSale")
    public long getApplyFlashSale() {
        return applyFlashSale;
    }

    @JsonProperty("applyFlashSale")
    public void setApplyFlashSale(long value) {
        this.applyFlashSale = value;
    }

    @JsonProperty("lockWeenkend")
    public boolean getLockWeenkend() {
        return lockWeenkend;
    }

    @JsonProperty("lockWeenkend")
    public void setLockWeenkend(boolean value) {
        this.lockWeenkend = value;
    }

    @JsonProperty("lockSpecial")
    public boolean getLockSpecial() {
        return lockSpecial;
    }

    @JsonProperty("lockSpecial")
    public void setLockSpecial(boolean value) {
        this.lockSpecial = value;
    }
}

