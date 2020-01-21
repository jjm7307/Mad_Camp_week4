package com.madcamp.week4.adapters;

public class ModelMaps {


    private String place,rating,lat,lng, dist;

    public ModelMaps(String place, String rating, String lat, String lng, String dist) {
        this.place = place;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.dist = dist;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
}
