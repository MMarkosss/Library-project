package MarkLivraria.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice // 1. Diz ao Spring: "Fique escutando todos os Controllers"
public class GlobalExceptionHandler {

    // 2. Diz ao Spring: "Se der este erro de validação, chame este método"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorMessage>> handle400Error(MethodArgumentNotValidException ex) {

        // Pega apenas a lista de erros de validação
        List<FieldError> errors = ex.getFieldErrors();

        // Converte a lista de FieldError para a nossa lista limpa de DTO e devolve
        return ResponseEntity.badRequest().body(errors.stream().map(ValidationErrorMessage::new).toList());
    }

    // 3. Um Record DTO interno só para formatar a resposta de erro
    private record ValidationErrorMessage(String field, String message) {
        public ValidationErrorMessage(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handle404Error() {
        // Quando qualquer parte do sistema não encontrar um dado no banco, devolve 404 vazio!
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleBusinessRuleError(IllegalArgumentException ex) {
        // Devolve o status 400 e a mensagem que o Service enviou no "throw new..."
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    // 2. Captura Erros Internos do Servidor (NullPointer, falhas de conexão, etc)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handle500Error(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + ex.getLocalizedMessage());
    }
}