package com.travelappproject.model.hotel;

import java.io.Serializable;
import java.util.List;

public class Banner implements Serializable {
    List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
