package com.example.quickcashg18;

public abstract class JobPreferences extends Job {
    public JobPreferences(String jobTitle, double duration, double totalPay, String urgency, MyLocation location) {
        super(jobTitle, duration, totalPay, urgency, location);
    }

    public JobPreferences(String jobTitle, String duration, String totalPay, String urgency, MyLocation location) {
        super(jobTitle, duration, totalPay, urgency, location);
    }

    public abstract boolean acceptableJob(Job job);
}
