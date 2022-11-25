package com.example.quickcashg18;

import android.location.Location;

/**
 * The Location class but with a no-arg constructor defined.
 * This class is absolutely necessary for queries involving jobs.
 * The Firebase querying functions require that the involved classes
 * define no-argument constructors, but Location does not. Hence this class.
 */
public class MyLocation extends Location {
    public MyLocation() { super(""); }

    public MyLocation(String provider) {
        super(provider);
    }

    public MyLocation(Location l) {
        super(l);
    }

    @Override
    public void setElapsedRealtimeNanos(long time) {
        super.setElapsedRealtimeNanos(time);
    }
}
