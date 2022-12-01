package com.example.quickcashg18;

import java.io.Serializable;

/**
 * For Employee-like users to store their job preferences and
 * decide whether or not a given job suits their preferences.
 */
public abstract class JobPreferences extends Job implements Serializable {
    public JobPreferences(String jobTitle, double duration, double totalPay, String urgency, MyLocation location) {
        super(jobTitle, duration, totalPay, urgency, location);
    }

    public JobPreferences(String jobTitle, String duration, String totalPay, String urgency, MyLocation location) {
        super(jobTitle, duration, totalPay, urgency, location);
    }

    public JobPreferences() { super(); }

    public abstract boolean acceptableJob(Job job);
}
