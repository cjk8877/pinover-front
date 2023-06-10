package com.example.pictureplace;

import com.google.gson.annotations.SerializedName;

public class MapPinsDTO {


    @SerializedName("locationid")
    private String locationId;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public String getLocationId() {
        return locationId;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
