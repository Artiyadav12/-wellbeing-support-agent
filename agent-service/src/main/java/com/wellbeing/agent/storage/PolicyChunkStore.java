package com.wellbeing.agent.storage;

import com.wellbeing.agent.chunking.Chunk;

import java.util.List;

public interface PolicyChunkStore {

    /**
     * Persists one chunk along with its embedding vector.
     */
    void save(Chunk chunk, List<Float> embedding);

    /**
     * Finds the topK chunks whose embeddings are closest (by cosine distance)
     * to the given query embedding.
     */
    List<RetrievedChunk> search(List<Float> queryEmbedding, int topK);
}
