# conversation-service

Postgres-backed conversation and message persistence — mirrors what your
work chatbot's API does under the hood, rebuilt from scratch so you
understand the schema and flow end to end.

## Requirements
- Java 17+, Maven 3.8+
- Docker (for Postgres) — or point `application.yml` at any Postgres instance you already have

## Run it

```bash
# 1. Start Postgres
docker compose up -d

# 2. Run the service (creates tables automatically via ddl-auto: update)
mvn spring-boot:run
```

Runs on **http://localhost:8082**.

## Try it end to end

```bash
# 1. Start a conversation for an employee
curl -X POST http://localhost:8082/api/conversations \
  -H "Content-Type: application/json" \
  -d '{"employeeId": "EMP-1001"}'
# → note the "conversationId" in the response, use it below

CONV_ID="paste-the-id-here"

# 2. Append a user message
curl -X POST http://localhost:8082/api/conversations/$CONV_ID/messages \
  -H "Content-Type: application/json" \
  -d '{"role": "USER", "content": "Why is my claim CLM-4521 delayed?"}'

# 3. Append an assistant reply, linking it to the claim it discussed
curl -X POST http://localhost:8082/api/conversations/$CONV_ID/messages \
  -H "Content-Type: application/json" \
  -d '{"role": "ASSISTANT", "content": "It is awaiting an itemized receipt from your provider.", "referencedClaimId": "CLM-4521"}'

# 4. Read the whole thread back, in order
curl http://localhost:8082/api/conversations/$CONV_ID/messages
```

## Check the raw rows (worth doing at least once)

```bash
docker exec -it wellbeing-pg psql -U postgres -d wellbeing -c "SELECT * FROM messages;"
```

## Schema

- **conversations**: `id` (UUID), `employee_id`, `created_at`
- **messages**: `id` (UUID), `conversation_id` (FK), `role` (`USER`/`ASSISTANT`), `content`,
  `referenced_claim_id` (nullable — set when a turn discussed a specific claim), `created_at`

`ddl-auto: update` is fine for this learning project; a real service would use
Flyway or Liquibase migrations instead of letting Hibernate manage the schema.

## Next steps in the project
1. ~~Mock claims/policy service~~
2. ~~Postgres conversation persistence~~ (this)
3. **RAG over sample policy docs** — build this part yourself: chunk a few
   policy/FAQ documents, embed them, store in pgvector, retrieve + generate.
4. Agent with tool calling into the mock claims service (also yours to build)
5. Guardrails on coverage/eligibility answers
6. Wire this persistence service into the agent loop — every turn saved here
7. (Stretch) Expose the claims service's endpoints over MCP instead of plain REST
