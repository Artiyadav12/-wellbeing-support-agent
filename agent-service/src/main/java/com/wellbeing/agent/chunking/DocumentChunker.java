package com.wellbeing.agent.chunking;

import java.util.List;

public interface DocumentChunker {

    /**
     * Splits one document's raw text into chunks.
     *
     * @param documentName the source file name, e.g. "wellness-program-faq.md"
     * @param rawText      the full raw text of the document
     * @return one Chunk per logical section
     */
    List<Chunk> chunk(String documentName, String rawText);
}
