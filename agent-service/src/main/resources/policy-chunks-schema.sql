-- Run after CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS policy_chunks (
    id SERIAL PRIMARY KEY,
    document_name TEXT NOT NULL,
    heading TEXT NOT NULL,
    chunk_text TEXT NOT NULL,
    embedding VECTOR(768) NOT NULL,   -- 768 matches nomic-embed-text's output size
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- An index speeds up similarity search once you have more than a handful of
-- rows. Not strictly necessary yet for ~20 chunks, but worth having in place:
-- ivfflat requires specifying an operator class matching your distance metric.
-- <=> below is cosine distance, the same one you'll use in your retrieval query.
CREATE INDEX IF NOT EXISTS policy_chunks_embedding_idx
    ON policy_chunks
    USING ivfflat (embedding vector_cosine_ops)
    WITH (lists = 10);
