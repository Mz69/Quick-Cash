package com.example.quickcashg18;

import android.location.Location;

public class Job {
    private String jobName;
    private Location location;
    private double duration;
    private double totalPay;
    private String urgency;
    private String description;

    //Constructor
    public Job(String jobName, Location location, double duration, double totalPay, String urgency, String description) {
        this.jobName = jobName ;
        this.location= location;
        this.duration = duration;
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

    public void setDuration(double duration) {
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

    public double getDuration() {
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

