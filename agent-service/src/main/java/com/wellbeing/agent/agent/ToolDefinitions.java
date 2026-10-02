package com.wellbeing.agent.agent;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * The tools the model is told it can use. Each one is just a name, a
 * description, and a JSON-schema for its arguments. The model never runs
 * anything - it only asks for a tool by name, and your code decides what
 * that actually does.
 *
 * Notice what is NOT an argument: employeeId. Who the person is comes from the
 * authenticated session (here: the request), never from the model. If the model
 * could choose the employeeId, a user could just type "look up EMP-1001's plan".
 *
 * The descriptions matter a lot - they're how a small model decides which tool
 * to pick. Try rewording one later and watch the behavior change.
 */
@Component
public class ToolDefinitions {

    public static final String GET_CLAIM_STATUS = "get_claim_status";
    public static final String GET_MY_PLAN = "get_my_plan";
    public static final String CHECK_ELIGIBILITY = "check_eligibility";

    public List<Map<String, Object>> all() {
        return List.of(
                tool(GET_CLAIM_STATUS,
                        "Look up the current status of ONE claim by its claim ID (format CLM-1234). "
                                + "Use when the person mentions a specific claim ID.",
                        Map.of("claimId", Map.of(
                                "type", "string",
                                "description", "The claim ID, for example CLM-4521")),
                        List.of("claimId")),

                tool(GET_MY_PLAN,
                        "Get the plan tier, per-benefit coverage details and dependents for the person asking. "
                                + "Takes no arguments.",
                        Map.of(),
                        List.of()),

                tool(CHECK_ELIGIBILITY,
                        "Check whether the person is covered for one specific benefit under their own plan.",
                        Map.of("program", Map.of(
                                "type", "string",
                                "description", "Benefit code: MENTAL_HEALTH, DENTAL, WELLNESS_REIMBURSEMENT or VISION")),
                        List.of("program"))
        );
    }

    private Map<String, Object> tool(String name, String description,
                                     Map<String, Object> properties, List<String> required) {
        return Map.of(
                "type", "function",
                "function", Map.of(
                        "name", name,
                        "description", description,
                        "parameters", Map.of(
                                "type", "object",
                                "properties", properties,
                                "required", required)));
    }
}
