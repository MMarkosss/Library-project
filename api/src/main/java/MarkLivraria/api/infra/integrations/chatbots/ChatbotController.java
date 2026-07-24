package MarkLivraria.api.infra.integrations.chatbots;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponseDTO> chat(@RequestBody ChatMessageRequestDTO request) {
        String reply = chatbotService.sendMessageToGemini(request.message());
        return ResponseEntity.ok(new ChatMessageResponseDTO(reply));
    }
}
