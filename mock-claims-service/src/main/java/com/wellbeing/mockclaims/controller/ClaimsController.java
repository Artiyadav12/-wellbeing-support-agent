package com.wellbeing.mockclaims.controller;

import com.wellbeing.mockclaims.data.MockDataStore;
import com.wellbeing.mockclaims.model.Claim;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimsController {

    private final MockDataStore dataStore;

    public ClaimsController(MockDataStore dataStore) {
        this.dataStore = dataStore;
    }

    // GET /api/claims/CLM-4521
    @GetMapping("/{claimId}")
    public ResponseEntity<Claim> getClaim(@PathVariable String claimId) {
        return dataStore.findClaimById(claimId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/claims?employeeId=EMP-1001
    @GetMapping
    public ResponseEntity<List<Claim>> getClaimsForEmployee(@RequestParam String employeeId) {
        return ResponseEntity.ok(dataStore.findClaimsByEmployee(employeeId));
    }
}
