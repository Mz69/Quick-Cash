package com.example.quickcashg18;

public class Job {
    private String jobName;
    private String location;
    private int durationInHours;
    private int hourlyPay;
    private String urgency;

    //Constructor
    public Job(String jobName,String location, int durationInHours,int hourlyPay,String urgency) {
        this.jobName = jobName ;
        this.location= location;
        this.durationInHours=durationInHours;
        this.hourlyPay=hourlyPay;
        this.urgency=urgency;
    }

    //Setters for each variable
    public void setJobName(String jobName) {
        this.jobName = jobName ;
    }

    public void setLocation(String location){
        this.location=location;
    }

    public  void  setDurationInHours(int durationInHours){
        this.durationInHours = durationInHours;
    }

    public void setHourlyPay(int hourlyPay) {
        this.hourlyPay = hourlyPay;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }


    //getters for each variable
    public String getJobName() {
        return jobName;
    }

    public String getLocation() {
        return location;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public int getHourlyPay() {
        return hourlyPay;
    }

    public String getUrgency() {
        return urgency;
    }
}

