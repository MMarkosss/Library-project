package MarkLivraria.api.infra.integrations.chatbots;

import MarkLivraria.api.infra.integrations.geminis.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatbotService {

    private final RestClient restClient;

    @Value("${google.gemini.api.key}")
    private String apiKey;

    public ChatbotService(@Value("${google.gemini.api.url}") String apiUrl) {
        // O RestClient usará a URL exata do properties sem tentar reescrevê-la
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

    public String sendMessageToGemini(String userMessage) {
        var systemInstruction = new GeminiSystemInstruction(
                List.of(new GeminiPart("Você é o assistente virtual da MarkLivraria. Seja educado, objetivo e ajude os clientes com dúvidas gerais sobre livros. Responda de forma breve."))
        );

        var userContent = new GeminiContent("user", List.of(new GeminiPart(userMessage)));
        var requestPayload = new GeminiRequest(List.of(userContent), systemInstruction);

        GeminiResponse response = restClient.post()
                // Enviando a chave de forma segura no Header
                .header("x-goog-api-key", apiKey)
                .header("Content-Type", "application/json")
                .body(requestPayload)
                .retrieve()
                .body(GeminiResponse.class);

        if (response != null && !response.candidates().isEmpty()) {
            return response.candidates().get(0).content().parts().get(0).text();
        }

        return "Desculpe, o assistente está indisponível no momento.";
    }
}
