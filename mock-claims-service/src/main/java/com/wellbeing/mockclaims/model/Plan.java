package com.wellbeing.mockclaims.model;

import java.util.List;

public class Plan {

    private String employeeId;
    private String planId;
    private String planName;              // e.g. "Wellbeing Gold"
    private List<CoverageItem> coverage;  // per-benefit coverage details
    private List<String> dependents;

    public Plan() {
    }

    public Plan(String employeeId, String planId, String planName,
                List<CoverageItem> coverage, List<String> dependents) {
        this.employeeId = employeeId;
        this.planId = planId;
        this.planName = planName;
        this.coverage = coverage;
        this.dependents = dependents;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public List<CoverageItem> getCoverage() { return coverage; }
    public void setCoverage(List<CoverageItem> coverage) { this.coverage = coverage; }

    public List<String> getDependents() { return dependents; }
    public void setDependents(List<String> dependents) { this.dependents = dependents; }

    public static class CoverageItem {
        private String benefit;      // e.g. "MENTAL_HEALTH", "DENTAL", "WELLNESS_REIMBURSEMENT"
        private boolean covered;
        private String copay;        // e.g. "$20 per visit"
        private String annualLimit;  // e.g. "$1000/year"

        public CoverageItem() {
        }

        public CoverageItem(String benefit, boolean covered, String copay, String annualLimit) {
            this.benefit = benefit;
            this.covered = covered;
            this.copay = copay;
            this.annualLimit = annualLimit;
        }

        public String getBenefit() { return benefit; }
        public void setBenefit(String benefit) { this.benefit = benefit; }

        public boolean isCovered() { return covered; }
        public void setCovered(boolean covered) { this.covered = covered; }

        public String getCopay() { return copay; }
        public void setCopay(String copay) { this.copay = copay; }

        public String getAnnualLimit() { return annualLimit; }
        public void setAnnualLimit(String annualLimit) { this.annualLimit = annualLimit; }
    }
}
