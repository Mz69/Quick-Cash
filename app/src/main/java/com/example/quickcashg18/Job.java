package com.example.quickcashg18;

import android.location.Location;

public class Job {
    private String jobName;
    private Location location;
    private int duration;
    private double totalPay;
    private String urgency;
    private String description;

    //Constructor
    public Job(String jobName, Location location, int duration, double totalPay, String urgency, String description) {
        this.jobName = jobName ;
        this.location= location;
        this.duration = duration;
        this.totalPay = totalPay;
        this.urgency = urgency;
        this.description = description;
    }

    public Job(String jobName, Location location, int durationHours, int durationMins, double totalPay, String urgency, String description) {
        this.jobName = jobName;
        this.location = location;
        this.duration = getDurationFromHoursAndMinutes(durationHours, durationMins);
        this.totalPay = totalPay;
        this.urgency = urgency;
        this.description = description;
    }

    //Setters for each variable
    public void setJobName(String jobName) {
        this.jobName = jobName ;
    }

    public void setLocation(Location location){
        this.location=location;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTotalPay(int totalPay) {
        this.totalPay = totalPay;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //getters for each variable
    public String getJobName() {
        return jobName;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Gets the number of hours in a duration of minutes,
     * with extra minutes cut off.
     * Example: 195 minutes returns 3 hours.
     */
    public int getDurationHours() {
        return duration / 60;
    }

    /**
     * Gets the number of minutes from the total duration after
     * removing the number of hours
     */
    public int getDurationRemainderMins() {
        return duration % 60;
    }

    public static int getDurationFromHoursAndMinutes(int hours, int minutes) {
        return hours * 60 + minutes;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public String getUrgency() {
        return urgency;
    }
}

