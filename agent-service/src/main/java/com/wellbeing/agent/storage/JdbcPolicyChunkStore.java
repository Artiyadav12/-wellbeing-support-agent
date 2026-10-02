package com.wellbeing.agent.storage;

import com.wellbeing.agent.chunking.Chunk;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcPolicyChunkStore implements PolicyChunkStore {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPolicyChunkStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Chunk chunk, List<Float> embedding) {
        jdbcTemplate.update(
                "INSERT INTO policy_chunks (document_name, heading, chunk_text, embedding) " +
                        "VALUES (?, ?, ?, ?::vector)",
                chunk.documentName(), chunk.heading(), chunk.text(), toVectorLiteral(embedding)
        );
    }

    @Override
    public List<RetrievedChunk> search(List<Float> queryEmbedding, int topK) {
        String sql = "SELECT document_name, heading, chunk_text, " +
                "embedding <=> ?::vector AS distance " +
                "FROM policy_chunks " +
                "ORDER BY distance ASC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new RetrievedChunk(
                rs.getString("document_name"),
                rs.getString("heading"),
                rs.getString("chunk_text"),
                rs.getDouble("distance")
        ), toVectorLiteral(queryEmbedding), topK);
    }

    private String toVectorLiteral(List<Float> embedding) {
        return "[" + embedding.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","))
                + "]";
    }
}