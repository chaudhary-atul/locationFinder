package com.atulkumar4141.locationfinder;

import java.io.Serializable;

public class LocationCoordinate implements Serializable {
    public double longitude = -1;
    public double latitude = -1;

    public LocationCoordinate(double theLatitude, double theLongitude) {
        longitude = theLongitude;
        latitude = theLatitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
