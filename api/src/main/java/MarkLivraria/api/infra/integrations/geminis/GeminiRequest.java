package MarkLivraria.api.infra.integrations.geminis;

import java.util.List;

public record GeminiRequest(
        List<GeminiContent> contents,
        GeminiSystemInstruction systemInstruction
) {}
