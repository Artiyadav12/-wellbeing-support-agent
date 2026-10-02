package com.wellbeing.mockclaims.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Claim {

    private String claimId;
    private String employeeId;
    private String type;          // e.g. "MEDICAL", "MENTAL_HEALTH", "DENTAL"
    private String status;        // e.g. "SUBMITTED", "IN_REVIEW", "APPROVED", "REJECTED", "PAID"
    private LocalDate submittedDate;
    private BigDecimal claimedAmount;
    private BigDecimal approvedAmount;
    private String description;
    private String statusNote;    // human-readable reason, e.g. "Awaiting itemized receipt"

    public Claim() {
    }

    public Claim(String claimId, String employeeId, String type, String status,
                 LocalDate submittedDate, BigDecimal claimedAmount, BigDecimal approvedAmount,
                 String description, String statusNote) {
        this.claimId = claimId;
        this.employeeId = employeeId;
        this.type = type;
        this.status = status;
        this.submittedDate = submittedDate;
        this.claimedAmount = claimedAmount;
        this.approvedAmount = approvedAmount;
        this.description = description;
        this.statusNote = statusNote;
    }

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }

    public BigDecimal getClaimedAmount() { return claimedAmount; }
    public void setClaimedAmount(BigDecimal claimedAmount) { this.claimedAmount = claimedAmount; }

    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatusNote() { return statusNote; }
    public void setStatusNote(String statusNote) { this.statusNote = statusNote; }
}
