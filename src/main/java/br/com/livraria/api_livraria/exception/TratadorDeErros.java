package br.com.livraria.api_livraria.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice // 1. Diz ao Spring: "Fique escutando todos os Controllers"
public class TratadorDeErros {

    // 2. Diz ao Spring: "Se der este erro de validação, chame este método"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {

        // Pega apenas a lista de erros de validação
        List<FieldError> erros = ex.getFieldErrors();

        // Converte a lista de FieldError para a nossa lista limpa de DTO e devolve
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    // 3. Um Record DTO interno só para formatar a resposta de erro
    private record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}