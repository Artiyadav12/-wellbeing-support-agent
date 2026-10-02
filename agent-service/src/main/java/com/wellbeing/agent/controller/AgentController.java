package com.wellbeing.agent.controller;

import com.wellbeing.agent.agent.AgentService;
import com.wellbeing.agent.dto.AgentRequest;
import com.wellbeing.agent.dto.AgentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    // POST /api/agent  { "employeeId": "EMP-1001", "question": "why is claim CLM-4521 delayed?" }
    @PostMapping
    public AgentResponse ask(@Valid @RequestBody AgentRequest request) {
        return agentService.run(request.employeeId(), request.question());
    }
}
