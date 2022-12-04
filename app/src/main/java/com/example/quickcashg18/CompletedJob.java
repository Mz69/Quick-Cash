package com.example.quickcashg18;

public class CompletedJob extends PostedJob {
    private String completerID;
    private boolean isPaid;

    public CompletedJob(PostedJob job, String completerID) {
        super(job);
        this.completerID = completerID;
        this.isPaid = false;
    }

    public void setCompleterID(String completerID) {
        this.completerID = completerID;
    }

    public void setPaid(boolean paid) {
        this.isPaid = paid;
    }

    public void setPaid() {
        this.isPaid = true;
    }

    public String getCompleterID() { return completerID; }
    public boolean getIsPaid() { return isPaid; }

    public boolean isPaid() { return getIsPaid(); }
}
