package com.example.quickcashg18;

import android.location.Location;

public class Job {
    private String jobTitle;
    private MyLocation location;
    private double duration;
    private double totalPay;
    private String urgency;
    private String description;
    private String posterID;
    private String jobID;
    private static final String JOB_ID_CONNECT = " BY_USER ";

    public Job() {}

    //Constructor
    public Job(String jobTitle, MyLocation location, double duration, double totalPay,
               String urgency, String description, String posterID) {
        this.jobTitle = jobTitle ;
        this.location= location;
        this.duration = duration;
        this.totalPay = totalPay;
        this.urgency = urgency;
        this.description = description;
        this.posterID = posterID;
        this.jobID = this.jobTitle + JOB_ID_CONNECT + this.posterID;
    }

    //Setters for each variable
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        updateJobID();
    }

    public void setLocation(MyLocation location){
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

    public void setPosterID(String posterID) {
        this.posterID = posterID;
        updateJobID();
    }

    public void updateJobID() { this.jobID = getJobTitle() + JOB_ID_CONNECT + getPosterID(); }

    //getters for each variable
    public String getJobTitle() {
        return jobTitle;
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

    public String getPosterID() { return posterID; }

    public String getJobID() { return jobID; }

    public String toString() { return jobTitle; }
}

