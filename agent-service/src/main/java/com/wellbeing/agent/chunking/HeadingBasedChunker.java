package com.wellbeing.agent.chunking;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: implement heading-based chunking.
 *
 * The approach (walked through in conversation, implement it here):
 *
 * 1. Split rawText wherever a line starts with "## " (a level-2 Markdown heading).
 *    Hint: rawText.split("(?m)^## ") using a multiline regex split is one way,
 *    but feel free to do it with a manual loop over lines instead - whichever
 *    helps you actually understand what's happening.
 *
 * 2. For each resulting piece, the first line is the heading text itself
 *    (everything up to the newline) and the rest is the body.
 *
 * 3. Build one Chunk per section: new Chunk(documentName, headingText, headingText + "\n" + body).
 *    Keep the heading glued into the chunk's text - it carries meaning on its own.
 *
 * 4. Skip any leading content before the first "## " heading (e.g. the "# Title"
 *    line) - it's not a retrievable section on its own.
 *
 * 5. (Optional stretch, once the basic version works): if any single chunk's
 *    body is very long (~400+ words), split it further on paragraph breaks
 *    ("\n\n") instead of returning it as one giant chunk.
 *
 * Test it with DocumentChunkerController (see controller package) before
 * moving on to embeddings - confirm the chunks look right by eye first.
 */
@Component
public class HeadingBasedChunker implements DocumentChunker {

    @Override
    public List<Chunk> chunk(String documentName, String rawText) {
        List<Chunk> chunks = new ArrayList<>();
        String[] lines = rawText.split("\n");

        String currentHeading = null;
        StringBuilder currentBody = new StringBuilder();

        for (String line : lines) {
            if (line.startsWith("## ")) {
                // We hit a new heading. If we were already building a chunk
                // (i.e. currentHeading isn't null), that chunk is done -
                // save it before starting the new one.
                if (currentHeading != null) {
                    // TODO: build a Chunk from currentHeading + currentBody
                    // and add it to `chunks`. Hint: chunks.add(new Chunk(...))

                    chunks.add(new Chunk(documentName, currentHeading, currentHeading + "\n" + currentBody));
                }

                // TODO: set currentHeading to the heading text, stripped of "## "
                // Hint: line.substring(3)
                currentHeading=line.substring(3);

                currentBody = new StringBuilder(); // reset body for the new section
            } else if (currentHeading != null) {
                // We're inside a section - accumulate this line into the body.
                // TODO: append `line` (plus a newline) to currentBody
                currentBody.append(line).append("\n");
            }
            // Lines before the first "## " heading (e.g. "# Title") are
            // silently skipped, since currentHeading is still null.
        }

        // after the loop - the last section never got a "next heading" to trigger its save
        if (currentHeading != null) {
            chunks.add(new Chunk(documentName, currentHeading, currentHeading + "\n" + currentBody));
        }

        return chunks;

    }
}
