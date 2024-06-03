package org.example.demo;

public class CoordsPair {
    private double lat;
    private double lng;

    public CoordsPair(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        //return STR."CoordsPair{lat=\{lat}, lng=\{lng}\{'}'}";
        return "CoordsPair [lat=" + lat + ", lng=" + lng + "]";
    }
}
