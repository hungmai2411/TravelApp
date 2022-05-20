package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;
import java.io.Serializable;

public enum HotelImageType implements Serializable {
    ALL, OTHER_HOTEL_IMAGE, ROOM_IMAGE;

    @JsonValue
    public String toValue() {
        switch (this) {
            case ALL:
                return "all";
            case OTHER_HOTEL_IMAGE:
                return "other_hotel_image";
            case ROOM_IMAGE:
                return "room_image";
        }
        return null;
    }

    @JsonCreator
    public static HotelImageType forValue(String value) throws IOException {
        if (value.equals("all")) return ALL;
        if (value.equals("other_hotel_image")) return OTHER_HOTEL_IMAGE;
        if (value.equals("room_image")) return ROOM_IMAGE;
        throw new IOException("Cannot deserialize HotelImageType");
    }
}
