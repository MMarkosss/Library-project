package MarkLivraria.api.infra.integrations.geminis;

import java.util.List;

public record GeminiSystemInstruction(List<GeminiPart> parts) {}
