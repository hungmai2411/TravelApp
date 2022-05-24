package com.travelappproject.model.hotel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.IOException;
import java.io.Serializable;

public enum ImageType implements Serializable {
    HOTEL_IMAGE;

    @JsonValue
    public String toValue() {
        switch (this) {
            case HOTEL_IMAGE:
                return "hotel_image";
        }
        return null;
    }

    @JsonCreator
    public static ImageType forValue(String value) throws IOException {
        if (value.equals("hotel_image")) return HOTEL_IMAGE;
        throw new IOException("Cannot deserialize ImageType");
    }
}
