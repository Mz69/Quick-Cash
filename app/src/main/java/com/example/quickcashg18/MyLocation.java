package com.example.quickcashg18;

import android.location.Location;

import java.io.Serializable;

/**
 * Handles user location based on latitude and longitude alone.
 *
 * Unfortunately, the Location class does not implement Serializable.
 * We serialize job preferences to implement filters in job search.
 * However, since Location does not implement Serializable, we either need
 * to make a Location subclass that does implement Serializable.
 * The subclass can then be serialized. Testing revealed that this serialization
 * is incomplete; in particular, the location will get initialized to
 * a basic location at latitude/longitude 0, instead of what the user preferred,
 * since Location does not implement Serializable.
 *
 * Thus, this class is implemented as a serializable simplification of Location
 * with the necessary methods, such as distance computation, taken by
 * converting the latitude and longitude into an actual Location and using the
 * Location class's equivalent methods.
 */
public class MyLocation implements Serializable {

    private double latitude;
    private double longitude;

    public MyLocation() {
        this.latitude = 0;
        this.longitude = 0; }

    public MyLocation(Location l) {
        this.latitude = l.getLatitude();
        this.longitude = l.getLongitude();
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Returns distance in KM between two points
     */
    public double getDistance(MyLocation l) {
        return convertToLocation().distanceTo(l.convertToLocation()) / 1000;
    }

    public Location convertToLocation() {
        Location l = new Location("");
        l.setLatitude(getLatitude());
        l.setLongitude(getLongitude());
        return l;
    }

    public String toString() {
        return "latitude: " + getLatitude() + "longitude: " + getLongitude();
    }
}
