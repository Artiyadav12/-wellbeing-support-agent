package com.wellbeing.agent.agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellbeing.agent.claims.ClaimsClient;
import com.wellbeing.agent.dto.AgentResponse;
import com.wellbeing.agent.llm.OllamaAgentClient;
import com.wellbeing.agent.llm.OllamaToolDtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AgentService {

    private static final Logger log = LoggerFactory.getLogger(AgentService.class);
    private static final int MAX_STEPS = 5;

    private final OllamaAgentClient llm;
    private final ToolDefinitions toolDefinitions;
    private final ClaimsClient claimsClient;
    private final ObjectMapper objectMapper;

    public AgentService(OllamaAgentClient llm,
                        ToolDefinitions toolDefinitions,
                        ClaimsClient claimsClient,
                        ObjectMapper objectMapper) {
        this.llm = llm;
        this.toolDefinitions = toolDefinitions;
        this.claimsClient = claimsClient;
        this.objectMapper = objectMapper;
    }

    public AgentResponse run(String employeeId, String question) {
        List<OllamaToolDtos.Message> messages = new ArrayList<>();
        messages.add(OllamaToolDtos.Message.system(systemPrompt()));
        messages.add(OllamaToolDtos.Message.user(question));
        List<String> trace = new ArrayList<>();

        for (int step = 0; step < MAX_STEPS; step++) {
            OllamaToolDtos.Message reply = llm.chat(messages, toolDefinitions.all());
            messages.add(reply);

            if (reply.toolCalls() == null || reply.toolCalls().isEmpty()) {
                String answer = (reply.content() == null || reply.content().isBlank())
                        ? "Sorry, I couldn't put together an answer. Please contact plan support."
                        : reply.content();
                return new AgentResponse(answer, trace);
            }

            for (OllamaToolDtos.ToolCall call : reply.toolCalls()) {
                String name = call.function().name();
                Map<String, Object> args = call.function().arguments() == null
                        ? Map.of()
                        : call.function().arguments();

                String result;
                try {
                    result = executeTool(name, args, employeeId);
                } catch (Exception e) {
                    log.warn("Tool {} failed", name, e);
                    result = "Tool error: the claims system is currently unavailable.";
                }

                trace.add(name + " " + args);
                messages.add(OllamaToolDtos.Message.tool(name, result));
            }
        }

        return new AgentResponse(
                "Sorry, I couldn't complete that request. Please try again or contact plan support.",
                trace);
    }

    private String executeTool(String name, Map<String, Object> args, String employeeId) {
        return switch (name) {
            case ToolDefinitions.GET_CLAIM_STATUS -> {
                String claimId = stringArg(args, "claimId");
                if (claimId.isEmpty()) {
                    yield "Missing claimId. Ask the person for the claim ID.";
                }
                Map<String, Object> claim = claimsClient.getClaim(claimId);
                if (claim == null || !employeeId.equals(String.valueOf(claim.get("employeeId")))) {
                    yield "No claim found with ID " + claimId + " for this employee.";
                }
                yield toJson(claim);
            }
            case ToolDefinitions.GET_MY_PLAN -> {
                Map<String, Object> plan = claimsClient.getPlan(employeeId);
                yield plan == null ? "No plan found for this employee." : toJson(plan);
            }
            case ToolDefinitions.CHECK_ELIGIBILITY -> {
                String program = stringArg(args, "program");
                if (program.isEmpty()) {
                    yield "Missing program. Valid values: MENTAL_HEALTH, DENTAL, WELLNESS_REIMBURSEMENT, VISION.";
                }
                yield toJson(claimsClient.checkEligibility(employeeId, program));
            }
            default -> "Unknown tool: " + name;
        };
    }

    private String systemPrompt() {
        return """
                You are a support assistant for an employee wellbeing and insurance program.
                Rules:
                1. You already know who the employee is. Never ask for their employee ID.
                2. For claim status, plan details, or eligibility, call a tool. Never guess.
                3. Only report facts a tool returned. Never invent claim details, amounts, or dates.
                4. If a tool finds nothing, say so plainly.
                5. Do not make final coverage or eligibility decisions. Report what the system
                   shows and suggest confirming with the plan administrator.
                6. If no tool fits the question, say you can't help with that here.
                7. Keep answers short.
                """;
    }

    private String stringArg(Map<String, Object> args, String key) {
        Object value = args.get(key);
        return value == null ? "" : value.toString().trim();
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }
}