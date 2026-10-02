package com.wellbeing.agent.storage;

/**
 * @param distance cosine distance to the query vector - smaller means more similar.
 *                 0 would mean identical direction, 2 is maximally dissimilar.
 */
public record RetrievedChunk(String documentName, String heading, String chunkText, double distance) {
}
