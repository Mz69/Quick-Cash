package com.example.quickcashg18;

public class Job {
    private String jobTitle;
    private double totalPay;
    private double duration;
    private MyLocation location;
    private String urgency;
    public static final double MAX = 999999999;

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
        this.duration = MAX;
        if (Validation.isNumeric(duration)) {
            this.duration = Double.parseDouble(duration);
        }
        this.totalPay = MAX;
        if (Validation.isNumeric(totalPay)) {
            this.totalPay = Double.parseDouble(totalPay);
        }
        this.urgency = urgency;
        this.location = location;
    }

    //Setters for each variable
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLocation(MyLocation location){
        this.location=location;
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

    public MyLocation getLocation() {
        return location;
    }

    public double getDuration() { return duration; }

    public double getTotalPay() { return totalPay; }

    public String getUrgency() {
        return urgency;
    }

    public String toString() { return jobTitle; }
}

