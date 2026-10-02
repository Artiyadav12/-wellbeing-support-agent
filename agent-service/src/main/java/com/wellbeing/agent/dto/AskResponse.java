package com.wellbeing.agent.dto;

import com.wellbeing.agent.storage.RetrievedChunk;

import java.util.List;

/**
 * @param answer  the LLM's generated answer
 * @param sources the chunks that were retrieved and given to the LLM - returned
 *                so you can always see WHY it answered the way it did
 */
public record AskResponse(String answer, List<RetrievedChunk> sources) {
}
