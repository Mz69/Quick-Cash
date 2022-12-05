package com.example.quickcashg18;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeePreferredJobUnitTest {

    private String validTitle = "Spaghetti Chef";
    private String validDuration = "40.50";
    private String validTotalPay = "35.99";
    private String validIsUrgent = "Urgent";
    private String validIsNotUrgent = "Not Urgent";
    private MyLocation validLocation = new MyLocation();
    private String validMaxDistance = "2.5";

    @Test
    public void checkGettersValidInput() {
        EmployeePreferredJob pref = new EmployeePreferredJob(validTitle, validDuration,
                validTotalPay, validIsUrgent, validLocation, validMaxDistance);
        assertEquals("Title incorrect", validTitle, pref.getJobTitle());
        assert Double.parseDouble(validDuration) == pref.getDuration();
        assert Double.parseDouble(validTotalPay) == pref.getTotalPay();
        assertEquals("Urgency incorrect", validIsUrgent, pref.getUrgency());
        assertEquals("Location incorrect", validLocation, pref.getMyLocation());
        assert Double.parseDouble(validMaxDistance) == pref.getMaxDistance();
    }

    @Test
    public void unacceptableJobTitle() {
        EmployeePreferredJob pref = new EmployeePreferredJob(validTitle, validDuration,
                validTotalPay, validIsUrgent, validLocation, validMaxDistance);
        Job job = new Job("not an acceptable title", 0, 0, "Urgent", new MyLocation());
        assertFalse(pref.acceptableJob(job));
    }

    @Test
    public void unacceptableDuration() {
        EmployeePreferredJob pref = new EmployeePreferredJob(validTitle, validDuration,
                validTotalPay, validIsUrgent, validLocation, validMaxDistance);
        Job job = new Job(validTitle, 500, 0, "Urgent", new MyLocation());
        assertFalse(pref.acceptableJob(job));
    }

    @Test
    public void unacceptableTotalPay() {
        EmployeePreferredJob pref = new EmployeePreferredJob(validTitle, validDuration,
                validTotalPay, validIsUrgent, validLocation, validMaxDistance);
        Job job = new Job(validTitle, validDuration, "0", "Urgent", new MyLocation());
        assertFalse(pref.acceptableJob(job));
    }

    @Test
    public void unacceptableUrgency() {
        EmployeePreferredJob pref = new EmployeePreferredJob(validTitle, validDuration,
                validTotalPay, validIsUrgent, validLocation, validMaxDistance);
        Job job = new Job(validTitle, validDuration, validTotalPay, "Not Urgent", new MyLocation());
        assertFalse(pref.acceptableJob(job));
    }

}
