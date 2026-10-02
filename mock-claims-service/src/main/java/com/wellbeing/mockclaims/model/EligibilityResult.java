package com.wellbeing.mockclaims.model;

public class EligibilityResult {

    private String employeeId;
    private String program;
    private boolean eligible;
    private String reason;

    public EligibilityResult() {
    }

    public EligibilityResult(String employeeId, String program, boolean eligible, String reason) {
        this.employeeId = employeeId;
        this.program = program;
        this.eligible = eligible;
        this.reason = reason;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public boolean isEligible() { return eligible; }
    public void setEligible(boolean eligible) { this.eligible = eligible; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
