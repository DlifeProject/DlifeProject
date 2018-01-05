package com.kang.Dlife.tb_page2.diary_view;

import java.io.Serializable;
import java.text.NumberFormat;

public class PlaceSpot implements Serializable {
    private double latitude, longitude;

    public PlaceSpot(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
