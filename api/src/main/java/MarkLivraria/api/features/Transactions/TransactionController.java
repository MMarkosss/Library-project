package MarkLivraria.api.features.Transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    // A mágica acontece aqui: O Spring Security injeta o 'Principal'
    @PostMapping("/comprar/{livroId}")
    public ResponseEntity<String> comprar(@PathVariable Long livroId, Principal principal) {

        // principal.getName() devolve exatamente o 'subject' que gravamos no Token (o e-mail!)
        String emailUsuario = principal.getName();

        transactionService.efetuarCompra(emailUsuario, livroId);

        return ResponseEntity.ok("Compra realizada com sucesso!");
    }

    @PostMapping("/emprestar/{livroId}")
    public ResponseEntity<String> emprestar(@PathVariable Long livroId, Principal principal) {

        String emailUsuario = principal.getName();

        transactionService.efetuarEmprestimo(emailUsuario, livroId);

        return ResponseEntity.ok("Empréstimo realizado com sucesso! O prazo de devolução é de 14 dias.");
    }
}
