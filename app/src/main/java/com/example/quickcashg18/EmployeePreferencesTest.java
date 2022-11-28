package com.example.quickcashg18;

public class EmployeePreferencesTest {

    private String jobTitle;
    private String minTotalPay;
    private String maxDuration;
    private String maxDistance;
    private String latitude;
    private String longitude;

    public EmployeePreferencesTest() {}

    public EmployeePreferencesTest(String jobTitle, String minTotalPay, String maxDuration,
                                   String maxDistance, String latitude, String longitude) {
        this.jobTitle = jobTitle;
        this.minTotalPay = minTotalPay;
        this.maxDuration = maxDuration;
        this.maxDistance = maxDistance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getJobTitle() { return jobTitle; }
    public String getMinTotalPay() { return minTotalPay; }
    public String getMaxDuration() { return maxDuration; }
    public String getMaxDistance() { return maxDistance; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }

    public String toString() {
        return getJobTitle() + "\n" + getMinTotalPay() + "\n" +
                getMaxDuration() + "\n" + getMaxDistance() + "\n" +
                getLatitude() + "\n" + getLongitude();
    }
}
