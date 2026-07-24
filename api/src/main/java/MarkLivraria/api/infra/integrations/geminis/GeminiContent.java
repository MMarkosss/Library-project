package MarkLivraria.api.infra.integrations.geminis;

import java.util.List;

public record GeminiContent(String role, List<GeminiPart> parts) {}
