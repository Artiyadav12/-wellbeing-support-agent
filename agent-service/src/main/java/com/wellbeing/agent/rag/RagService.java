package com.wellbeing.agent.rag;

import com.wellbeing.agent.dto.AskResponse;
import com.wellbeing.agent.embedding.EmbeddingClient;
import com.wellbeing.agent.llm.LlmClient;
import com.wellbeing.agent.storage.PolicyChunkStore;
import com.wellbeing.agent.storage.RetrievedChunk;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The full RAG flow: embed the question, retrieve chunks, build a prompt
 * around them, ask the LLM. The orchestration in answer() is done; the two
 * prompt-building methods are yours.
 */
@Service
public class RagService {

    private final EmbeddingClient embeddingClient;
    private final PolicyChunkStore policyChunkStore;
    private final LlmClient llmClient;

    public RagService(EmbeddingClient embeddingClient,
                      PolicyChunkStore policyChunkStore,
                      LlmClient llmClient) {
        this.embeddingClient = embeddingClient;
        this.policyChunkStore = policyChunkStore;
        this.llmClient = llmClient;
    }

    public AskResponse answer(String question) {
        List<Float> queryEmbedding = embeddingClient.embed(question);
        List<RetrievedChunk> chunks = policyChunkStore.search(queryEmbedding, 3);

        String systemPrompt = buildSystemPrompt();
        String userPrompt = buildUserPrompt(question, chunks);

        String answer = llmClient.chat(systemPrompt, userPrompt);
        return new AskResponse(answer, chunks);
    }

    /**
     * TODO: write the system prompt - the standing rules for the assistant.
     *
     * It should make the model:
     * - answer ONLY from the provided context, not from its own general knowledge
     * - say clearly that it doesn't know (and suggest contacting plan support)
     *   when the context doesn't contain the answer
     * - ignore context sections that aren't relevant to the question
     *   (retrieval always returns 3 chunks, even when only one is useful)
     * - never state a definitive coverage or eligibility decision - point to what
     *   the policy text says and tell the person to confirm with their plan administrator
     * - mention which section(s) the answer came from
     *
     * Write it in your own words and iterate on it. Small changes to this text
     * change the behavior a lot - that's the point of the exercise.
     */
    private String buildSystemPrompt() {
        return """
            You are a support assistant for an employee wellbeing and insurance program.
            Follow these rules:
            1. Answer using only the context provided. Never use outside knowledge.
            2. If the context does not contain the answer, say you don't know and suggest
               contacting plan support. Never guess or invent details such as phone numbers,
               amounts, or dates.
            3. If the answer differs between the Gold and Silver plans and the question does
               not say which plan the person has, give both answers or ask which plan they
               are on. Never pick one silently.
            4. Do not make final coverage or eligibility decisions. Describe what the policy
               says and tell the person to confirm with their plan administrator.
            5. Ignore context that is not relevant to the question.
            6. End by naming the section(s) your answer came from.
            """;
    }
    /**
     * TODO: build the user prompt - the retrieved context plus the question.
     *
     * Roughly: label and list each chunk (include its documentName and heading,
     * so the model can cite sections), then put the question after the context.
     * Use a clear separator between chunks so the model can tell where one ends
     * and the next begins.
     */
    private String buildUserPrompt(String question, List<RetrievedChunk> chunks) {
        StringBuilder sb = new StringBuilder("Context:\n\n");
        for (RetrievedChunk chunk : chunks) {
            sb.append("--- Source: ").append(chunk.documentName())
                    .append(" / ").append(chunk.heading()).append(" ---\n");
            sb.append(chunk.chunkText()).append("\n\n");
        }
        sb.append("Question: ").append(question);
        return sb.toString();
    }
}
