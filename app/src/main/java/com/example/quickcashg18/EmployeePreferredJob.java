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

    // Invalid string arguments default to another value!
    // For example, an invalid maximum distance defaults to the maximum
    // allowed distance.
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
                acceptableDuration(job) && acceptableUrgency(job) &&
                acceptableDistance(job);
    }

    private boolean acceptableJobTitle(Job job) {
        String prefTitle = getJobTitle().toLowerCase();
        String jobTitle = job.getJobTitle().toLowerCase();
        if (jobTitle.startsWith(prefTitle)) {
            return true;
        }

        String[] splitJobTitle = job.getJobTitle().toLowerCase().split(" ");
        for (String s : splitJobTitle) {
            if (s.startsWith(prefTitle)) {
                return true;
            }
        }
        return false;
    }

    private boolean acceptableTotalPay(Job job) {
        return getTotalPay() <= job.getTotalPay();
    }

    private boolean acceptableDuration(Job job) {
        return getDuration() >= job.getDuration();
    }

    /**
     * A job's urgency must be at least as urgent the
     * Employee's urgency.
     */
    private boolean acceptableUrgency(Job job) {
        // The "Urgent" here is a magic value.
        // If you're doing some refactoring, refactor this across the code!
        // Maybe create a file with "Urgent" and "Not Urgent" constants.
        if (getUrgency().equals("Urgent")) {
            return job.getUrgency().equals("Urgent");
        }
        return true;
    }

    private boolean acceptableDistance(Job job) {
        return getDistanceFrom(job) <= getMaxDistance();
    }

    public String toString() {
        return getJobTitle() + "\n" + getDuration() + "\n" +  getTotalPay() +
                "\n" + getUrgency() + "\n" + getMaxDistance();
    }
}
