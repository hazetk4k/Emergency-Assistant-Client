package com.example.eaclient.Models;

public class ReportApplicant {
    Report report;

    Applicant applicant;

    public Report getReport() {
        return report;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
