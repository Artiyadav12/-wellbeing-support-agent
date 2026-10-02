# agent-service

This is where the RAG + LLM + agent logic lives — the piece you're building
yourself to actually learn it. Everything here is scaffolding; the AI logic
is left as TODOs for you.

## Current state

- `chunking/DocumentChunker.java` — the interface
- `chunking/HeadingBasedChunker.java` — **implement this one.** Full approach
  is in the class comment. Don't skip straight to copying a library for this —
  the whole point is understanding what a chunker actually does.
- `controller/DocumentChunkerController.java` — a `/api/chunking/preview`
  endpoint so you can test your chunker directly, without needing embeddings
  or a database wired up yet.

## Run it

```bash
mvn spring-boot:run
```

Starts on **http://localhost:8083**.

## Test your chunker as you build it

Take the contents of `wellness-program-faq.md` (from the policy-docs you
already have) and paste it into a request like this:

```bash
curl -X POST http://localhost:8083/api/chunking/preview \
  -H "Content-Type: application/json" \
  -d '{
    "documentName": "wellness-program-faq.md",
    "rawText": "...paste the full file contents here, with \n for newlines..."
  }'
```

Easier in Postman than raw curl, since you won't have to manually escape
newlines — paste the file content into the body there instead.

**What to check in the response:**
- Do you get back one chunk per `##` section? (7 for the wellness FAQ)
- Does each chunk's `heading` field match the actual heading text, with no
  leading `#` characters or stray whitespace?
- Does each chunk's `text` field contain both the heading and its full body?
- Is anything before the first `##` heading (the `# Wellness Program FAQ`
  title line) correctly excluded?

Once this looks right for all three policy-docs files, you're ready for the
next step: generating embeddings for each chunk.

## Next steps in the project (this service)
1. **Chunking** (this — implement `HeadingBasedChunker`)
2. Generate embeddings for each chunk (plain HTTP call to OpenAI's
   embeddings endpoint — `RestClient` is enough, no new dependency needed)
3. Set up pgvector and store chunks + embeddings
4. Build the retrieval query (embed the question, similarity search)
5. Construct the RAG prompt and call the LLM
6. Add tool calling into `mock-claims-service` for live-data questions
7. Wire in `conversation-service` so every turn gets persisted
