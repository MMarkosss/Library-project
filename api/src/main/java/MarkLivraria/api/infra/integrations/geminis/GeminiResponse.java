package MarkLivraria.api.infra.integrations.geminis;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {
    public record Candidate(GeminiContent content) {}
}
