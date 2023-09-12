package com.colourmoon.gobuddy.model;

public class PayoutModel {

    private String jobId;
    private String jobName;
    private String jobDateTime;
    private String jobAmount;

    public PayoutModel(String jobId, String jobName, String jobDateTime, String jobAmount) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobDateTime = jobDateTime;
        this.jobAmount = jobAmount;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDateTime() {
        return jobDateTime;
    }

    public String getJobAmount() {
        return jobAmount;
    }
}
