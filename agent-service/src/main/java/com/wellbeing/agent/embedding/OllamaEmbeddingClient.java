package com.wellbeing.agent.embedding;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * TODO: implement the call to Ollama's embeddings endpoint.
 *
 * You already tested this exact call in Postman:
 *   POST http://localhost:11434/api/embeddings
 *   body: { "model": "nomic-embed-text", "prompt": "hello world" }
 *   response: { "embedding": [ ...768 floats... ] }
 *
 * Spring's RestClient (built in, no extra dependency needed) is the Java
 * equivalent of that Postman request. Rough shape of what you need to write:
 *
 *   RestClient restClient = RestClient.create("http://localhost:11434");
 *
 *   var request = new OllamaEmbeddingDtos.EmbeddingRequest("nomic-embed-text", text);
 *
 *   var response = restClient.post()
 *           .uri("/api/embeddings")
 *           .contentType(MediaType.APPLICATION_JSON)
 *           .body(request)
 *           .retrieve()
 *           .body(OllamaEmbeddingDtos.EmbeddingResponse.class);
 *
 *   return response.embedding();
 *
 * Things to think about while wiring this up, not just copy it in:
 * - What should happen if Ollama isn't running (connection refused)? Right
 *   now this whole app would just throw an unhandled exception up to the
 *   caller. Is that OK for now, or worth catching and wrapping in a clearer
 *   error message?
 * - RestClient.create(...) creates a new client every call here if you put
 *   it inside the method - better to build it once as a field so it's
 *   reused. Where would that go - a constructor, a @Bean, a static field?
 *
 * Test it with EmbeddingController (see controller package) once written.
 */
@Component
public class OllamaEmbeddingClient implements EmbeddingClient {

    @Override
    public List<Float> embed(String text) {

        RestClient restClient = RestClient.create("http://localhost:11434");
        var request = new OllamaEmbeddingDtos.EmbeddingRequest("nomic-embed-text", text);
        var response = restClient.post()
                .uri("/api/embeddings")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OllamaEmbeddingDtos.EmbeddingResponse.class);

        return response.embedding();
    }

}
