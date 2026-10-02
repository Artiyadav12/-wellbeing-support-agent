package com.wellbeing.mockclaims.controller;

import com.wellbeing.mockclaims.data.MockDataStore;
import com.wellbeing.mockclaims.model.EligibilityResult;
import com.wellbeing.mockclaims.model.Plan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PlanController {

    private final MockDataStore dataStore;

    public PlanController(MockDataStore dataStore) {
        this.dataStore = dataStore;
    }

    // GET /api/plans/EMP-1001
    @GetMapping("/plans/{employeeId}")
    public ResponseEntity<Plan> getPlan(@PathVariable String employeeId) {
        return dataStore.findPlanByEmployee(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/eligibility?employeeId=EMP-1001&program=WELLNESS_REIMBURSEMENT
    @GetMapping("/eligibility")
    public ResponseEntity<EligibilityResult> checkEligibility(
            @RequestParam String employeeId,
            @RequestParam String program) {
        return ResponseEntity.ok(dataStore.checkEligibility(employeeId, program));
    }
}
