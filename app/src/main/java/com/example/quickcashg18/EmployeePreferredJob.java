package com.example.quickcashg18;

import java.io.Serializable;

/**
 * Stores the preferred job parameters of an Employee.
 */
public class EmployeePreferredJob extends JobPreferences implements Serializable {
    private double maxDistance;

    public EmployeePreferredJob() { super(); }

    public EmployeePreferredJob(String jobTitle, double duration, double totalPay,
                                String urgency, MyLocation location, double maxDistance) {
        super(jobTitle, duration, totalPay, urgency, location);
        this.maxDistance = maxDistance;
    }

    public EmployeePreferredJob(String jobTitle, String duration, String totalPay,
                                String urgency, MyLocation location, String maxDistance) {
        super(jobTitle, duration, totalPay, urgency, location);
        this.maxDistance = Double.MAX_VALUE;
        if (Validation.isNumeric(maxDistance)) {
            this.maxDistance = Double.parseDouble(maxDistance);
        }
    }

    public double getMaxDistance() { return maxDistance; }

    @Override
    public boolean acceptableJob(Job job) {
        return acceptableJobTitle(job) && acceptableTotalPay(job) &&
                acceptableDuration(job) && acceptableDistance(job);
    }

    private boolean acceptableJobTitle(Job job) {
        String prefTitle = getJobTitle().toLowerCase();
        String jobTitle = job.getJobTitle().toLowerCase();
        if (jobTitle.startsWith(prefTitle)) {
            return true;
        }

        String splitJobTitle[] = job.getJobTitle().toLowerCase().split(" ");
        for (String s : splitJobTitle) {
            if (s.startsWith(prefTitle)) {
                return true;
            }
        }
        return false;
    }

    private boolean acceptableTotalPay(Job job) {
        return getTotalPay() >= job.getTotalPay();
    }

    private boolean acceptableDuration(Job job) {
        return getDuration() >= job.getDuration();
    }

    private boolean acceptableUrgency(Job job) {
        String prefUrgency = getUrgency();
        return prefUrgency.equals("") || getUrgency().equals(job.getUrgency());
    }

    private boolean acceptableDistance(Job job) {
        return true;
    }

    public String toString() {
        return getJobTitle() + "\n" + getDuration() + "\n" +  getTotalPay() +
                "\n" + getUrgency() + "\n" + getMaxDistance();
    }
}
