package com.wellbeing.agent.embedding;

import java.util.List;

public interface EmbeddingClient {

    /**
     * Turns a piece of text into its embedding vector.
     *
     * @param text the text to embed (e.g. one Chunk's text, or a user's question)
     * @return the embedding as a list of floats
     */
    List<Float> embed(String text);
}
