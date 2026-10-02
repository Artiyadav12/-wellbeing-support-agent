package com.wellbeing.agent.dto;

import java.util.List;

/**
 * @param answer    the agent's final reply
 * @param toolTrace one entry per tool the agent called, e.g. "get_claim_status {claimId=CLM-4521}".
 *                  Returned so you can always see what the agent actually did, not just what it said.
 */
public record AgentResponse(String answer, List<String> toolTrace) {
}
