package com.example.quickcashg18;

/**
 * Stores an Employer's preferences for job postings.
 */
public class EmployerPreferredJob extends Job {

    public EmployerPreferredJob() { super(); }

    public EmployerPreferredJob(String jobTitle, double duration, double totalPay,
                                String urgency, MyLocation location) {
        super(jobTitle, duration, totalPay, urgency, location);
    }

    public EmployerPreferredJob(String jobTitle, String duration, String totalPay,
                                String urgency, MyLocation location) {
        super(jobTitle, duration, totalPay, urgency, location);
    }

}
