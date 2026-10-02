# mock-claims-service

A fake claims/plan/eligibility backend, standing in for the real enterprise
system your AI agent will eventually call. In-memory data only — no database,
so it starts instantly and needs no setup beyond a JDK and Maven.

## Requirements
- Java 17+
- Maven 3.8+ (or use your IDE's built-in Maven support)

## Run it

```bash
mvn spring-boot:run
```

It starts on **http://localhost:8081**.

## Try it

```bash
# Get a specific claim (this is the "why is my claim delayed" case)
curl http://localhost:8081/api/claims/CLM-4521

# List all claims for an employee
curl "http://localhost:8081/api/claims?employeeId=EMP-1001"

# Get an employee's plan and coverage details
curl http://localhost:8081/api/plans/EMP-1001

# Check eligibility for a specific benefit
curl "http://localhost:8081/api/eligibility?employeeId=EMP-1001&program=WELLNESS_REIMBURSEMENT"

# A rejected claim, to see a different status/reason
curl http://localhost:8081/api/claims/CLM-4530

# An employee whose plan doesn't cover dental
curl "http://localhost:8081/api/eligibility?employeeId=EMP-1002&program=DENTAL"
```

## Sample data reference

| Employee ID | Plan | Claims |
|---|---|---|
| EMP-1001 | Wellbeing Gold | CLM-4521 (mental health, in review), CLM-4522 (dental, approved) |
| EMP-1002 | Wellbeing Silver | CLM-4530 (medical, rejected — out of network), CLM-4531 (wellness, paid) |

## Why this exists

This is step 1 of the wellbeing/insurance support agent project: a standalone
service the agent will call via tool calling for anything that needs *live*
data (claim status, eligibility) as opposed to *static* knowledge (how the
claims process works, what's generally covered) — which will come from RAG
over policy documents instead.

## Next steps in the project
1. ~~Mock claims/policy service~~ (this)
2. Postgres conversation persistence
3. RAG over sample policy docs
4. Agent with tool calling into this service
5. Guardrails on coverage/eligibility answers
6. Wire persistence into the agent loop
7. (Stretch) Expose these endpoints over MCP instead of plain REST
