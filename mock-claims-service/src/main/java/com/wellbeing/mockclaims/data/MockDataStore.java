package com.wellbeing.mockclaims.data;

import com.wellbeing.mockclaims.model.Claim;
import com.wellbeing.mockclaims.model.EligibilityResult;
import com.wellbeing.mockclaims.model.Plan;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Fake "database" of claims, plans, and eligibility rules, held in memory.
 * Stands in for the real enterprise claims/policy system this service would
 * normally call. Swap this out for a real PostgreSQL-backed repository once
 * you want to persist real data.
 */
@Component
public class MockDataStore {

    private final Map<String, Claim> claimsById = new HashMap<>();
    private final Map<String, List<Claim>> claimsByEmployee = new HashMap<>();
    private final Map<String, Plan> planByEmployee = new HashMap<>();

    public MockDataStore() {
        seedClaims();
        seedPlans();
    }

    private void seedClaims() {
        addClaim(new Claim("CLM-4521", "EMP-1001", "MENTAL_HEALTH", "IN_REVIEW",
                LocalDate.of(2026, 9, 10), new BigDecimal("180.00"), null,
                "Therapy session - 3 visits",
                "Awaiting itemized receipt from provider"));

        addClaim(new Claim("CLM-4522", "EMP-1001", "DENTAL", "APPROVED",
                LocalDate.of(2026, 8, 2), new BigDecimal("320.00"), new BigDecimal("280.00"),
                "Root canal procedure",
                "Approved, payment scheduled for next cycle"));

        addClaim(new Claim("CLM-4530", "EMP-1002", "MEDICAL", "REJECTED",
                LocalDate.of(2026, 7, 15), new BigDecimal("95.00"), BigDecimal.ZERO,
                "Out-of-network specialist visit",
                "Provider is out-of-network and plan does not cover out-of-network visits"));

        addClaim(new Claim("CLM-4531", "EMP-1002", "WELLNESS_REIMBURSEMENT", "PAID",
                LocalDate.of(2026, 6, 1), new BigDecimal("50.00"), new BigDecimal("50.00"),
                "Gym membership reimbursement - June",
                "Paid in July payroll cycle"));
    }

    private void seedPlans() {
        planByEmployee.put("EMP-1001", new Plan(
                "EMP-1001", "PLAN-GOLD", "Wellbeing Gold",
                List.of(
                        new Plan.CoverageItem("MENTAL_HEALTH", true, "$20 per visit", "$1500/year"),
                        new Plan.CoverageItem("DENTAL", true, "$0 preventive / 20% procedures", "$2000/year"),
                        new Plan.CoverageItem("WELLNESS_REIMBURSEMENT", true, "N/A", "$300/year"),
                        new Plan.CoverageItem("VISION", false, "N/A", "N/A")
                ),
                List.of("Spouse: Priya Sharma", "Child: Arjun Sharma")
        ));

        planByEmployee.put("EMP-1002", new Plan(
                "EMP-1002", "PLAN-SILVER", "Wellbeing Silver",
                List.of(
                        new Plan.CoverageItem("MENTAL_HEALTH", true, "$35 per visit", "$800/year"),
                        new Plan.CoverageItem("DENTAL", false, "N/A", "N/A"),
                        new Plan.CoverageItem("WELLNESS_REIMBURSEMENT", true, "N/A", "$150/year"),
                        new Plan.CoverageItem("VISION", false, "N/A", "N/A")
                ),
                List.of()
        ));
    }

    private void addClaim(Claim claim) {
        claimsById.put(claim.getClaimId(), claim);
        claimsByEmployee.computeIfAbsent(claim.getEmployeeId(), k -> new ArrayList<>()).add(claim);
    }

    public Optional<Claim> findClaimById(String claimId) {
        return Optional.ofNullable(claimsById.get(claimId));
    }

    public List<Claim> findClaimsByEmployee(String employeeId) {
        return claimsByEmployee.getOrDefault(employeeId, List.of());
    }

    public Optional<Plan> findPlanByEmployee(String employeeId) {
        return Optional.ofNullable(planByEmployee.get(employeeId));
    }

    public EligibilityResult checkEligibility(String employeeId, String program) {
        Plan plan = planByEmployee.get(employeeId);
        if (plan == null) {
            return new EligibilityResult(employeeId, program, false, "No plan found for this employee ID");
        }

        return plan.getCoverage().stream()
                .filter(item -> item.getBenefit().equalsIgnoreCase(program))
                .findFirst()
                .map(item -> item.isCovered()
                        ? new EligibilityResult(employeeId, program, true,
                            "Covered under " + plan.getPlanName() + ", limit " + item.getAnnualLimit())
                        : new EligibilityResult(employeeId, program, false,
                            "Not covered under " + plan.getPlanName()))
                .orElse(new EligibilityResult(employeeId, program, false,
                        "No coverage rule found for program: " + program));
    }
}
