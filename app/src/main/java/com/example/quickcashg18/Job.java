package com.example.quickcashg18;

import java.io.Serializable;

public class Job implements Serializable {
    private String jobTitle;
    private double totalPay;
    private double duration;
    private MyLocation location;
    private String urgency;

    public Job() {}

    //Constructor
    public Job(String jobTitle, double duration, double totalPay,
               String urgency, MyLocation location) {
        this.jobTitle = jobTitle;
        this.location = location;
        this.duration = duration;
        this.totalPay = totalPay;
        this.urgency = urgency;
    }

    public Job(String jobTitle, String duration, String totalPay,
               String urgency, MyLocation location) {
        this.jobTitle = jobTitle;
        this.duration = Double.MAX_VALUE;
        if (Validation.isNumeric(duration)) {
            this.duration = Double.parseDouble(duration);
        }
        this.totalPay = Double.MAX_VALUE;
        if (Validation.isNumeric(totalPay)) {
            this.totalPay = Double.parseDouble(totalPay);
        }
        this.urgency = urgency;
        this.location = location;
    }

    public Job(Job job) {
        this.jobTitle = job.getJobTitle();
        this.location = job.getMyLocation();
        this.totalPay = job.getTotalPay();
        this.duration = job.getDuration();
        this.urgency = job.getUrgency();
    }

    //Setters for each variable
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setMyLocation(MyLocation location){
        this.location = location;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    //getters for each variable
    public String getJobTitle() {
        return jobTitle;
    }

    public MyLocation getMyLocation() {
        return location;
    }

    public double getDuration() { return duration; }

    public double getTotalPay() { return totalPay; }

    public String getUrgency() {
        return urgency;
    }

    public double getDistanceFrom(Job job) {
        return getMyLocation().getDistance(job.getMyLocation());
    }

    public String toString() { return jobTitle; }
}

