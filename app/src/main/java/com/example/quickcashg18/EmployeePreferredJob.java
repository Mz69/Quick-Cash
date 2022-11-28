package com.example.quickcashg18;

public class EmployeePreferredJob extends JobPreferences {
    private double maxDistance;

    public EmployeePreferredJob(String jobTitle, double duration, double totalPay,
                                String urgency, MyLocation location, double maxDistance) {
        super(jobTitle, duration, totalPay, urgency, location);
        this.maxDistance = maxDistance;
    }

    public EmployeePreferredJob(String jobTitle, String duration, String totalPay,
                                String urgency, MyLocation location, String maxDistance) {
        super(jobTitle, duration, totalPay, urgency, location);
        this.maxDistance = MAX;
        if (Validation.isNumeric(maxDistance)) {
            this.maxDistance = Double.parseDouble(maxDistance);
        }
    }

    @Override
    public boolean acceptableJob(Job job) {
        return acceptableTotalPay(job) && acceptableDuration(job)
                && acceptableDistance(job);
    }

    private boolean acceptableTitle(Job job) {
        String prefTitle = getJobTitle();
        String jobTitle = job.getJobTitle();
        return prefTitle.equals("") ||
                jobTitle.toLowerCase().startsWith(prefTitle.toLowerCase());
    }

    private boolean acceptableTotalPay(Job job) {
        return getTotalPay() >= job.getTotalPay();
    }

    private boolean acceptableDuration(Job job) {
        return getDuration() <= job.getDuration();
    }

    private boolean acceptableUrgency(Job job) {
        String prefUrgency = getUrgency();
        return prefUrgency.equals("") || getUrgency().equals(job.getUrgency());
    }

    private boolean acceptableDistance(Job job) {
        return true;
    }
}
