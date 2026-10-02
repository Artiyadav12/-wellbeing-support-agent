package com.wellbeing.agent.chunking;

/**
 * One chunk of a source document, ready to be embedded and stored.
 *
 * @param documentName which source file this came from, e.g. "wellness-program-faq.md"
 * @param heading      the section heading this chunk belongs to, e.g. "How do I submit a wellness reimbursement claim?"
 * @param text         the chunk's full text, heading + body together
 */
public record Chunk(String documentName, String heading, String text) {
}
