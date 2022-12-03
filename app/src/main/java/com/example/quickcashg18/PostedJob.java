package com.example.quickcashg18;

public class PostedJob extends Job {

    private String posterID;
    private String jobID;
    private static final String JOB_ID_CONNECT = " BY_USER ";

    public PostedJob() { super(); }

    public PostedJob(String jobTitle, double duration, double totalPay,
                     String urgency, MyLocation location, String posterID) {
        super(jobTitle, duration, totalPay, urgency, location);
        this.posterID = posterID;
        this.jobID = getJobTitle() + JOB_ID_CONNECT + this.posterID;
    }

    public PostedJob(String jobTitle, String duration, String totalPay,
                     String urgency, MyLocation location, String posterID) {
        super(jobTitle, duration, totalPay, urgency, location);
        this.posterID = posterID;
        this.jobID = getJobTitle() + JOB_ID_CONNECT + this.posterID;
    }

    @Override
    public void setJobTitle(String jobTitle) {
        super.setJobTitle(jobTitle);
        updateJobID();
    }

    public void setPosterID(String posterID) {
        this.posterID = posterID;
        updateJobID();
    }

    public void updateJobID() { this.jobID = getJobTitle() + JOB_ID_CONNECT + getPosterID(); }

    public String getPosterID() { return posterID; }

    public String getJobID() { return jobID; }

}
