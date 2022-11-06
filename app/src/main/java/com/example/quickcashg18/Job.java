package com.example.quickcashg18;

public class Job {
    private String personName;
    private String jobDescription;
    private String durationInHours;
    private String hourlyPay;
    private String urgency;

    //Constructor
    public Job(String personName,String jobDescription,String durationInHours,String hourlyPay,String urgency) {
        this.personName = personName ;
        this.jobDescription=jobDescription;
        this.durationInHours=durationInHours;
        this.hourlyPay=hourlyPay;
        this.urgency=urgency;
    }

    //Setters for each variable
    public void setPersonName(String personName) {
        this.personName = personName ;
    }

    public void setJobDescription(String jobDescription){
        this.jobDescription=jobDescription;
    }

    public  void  setDurationInHours(String durationInHours){
        this.jobDescription =durationInHours;
    }

    public void setHourlyPay(String hourlyPay) {
        this.hourlyPay = hourlyPay;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }


    //getters for each variable
    public String getPersonName() {
        return personName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getDurationInHours() {
        return durationInHours;
    }

    public String getHourlyPay() {
        return hourlyPay;
    }

    public String getUrgency() {
        return urgency;
    }
}

