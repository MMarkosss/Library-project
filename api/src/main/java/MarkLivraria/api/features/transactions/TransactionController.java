package MarkLivraria.api.features.transactions;

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
    @PostMapping("/comprar/{bookId}")
    public ResponseEntity<String> purchase(@PathVariable Long bookId, Principal principal) {

        // principal.getName() devolve exatamente o 'subject' que gravamos no Token (o e-mail!)
        String userEmail = principal.getName();

        transactionService.executePurchase(userEmail, bookId);

        return ResponseEntity.ok("Compra realizada com sucesso!");
    }

    @PostMapping("/emprestar/{bookId}")
    public ResponseEntity<String> loan(@PathVariable Long bookId, Principal principal) {

        String userEmail = principal.getName();

        transactionService.executeLoan(userEmail, bookId);

        return ResponseEntity.ok("Empréstimo realizado com sucesso! O prazo de devolução é de 14 dias.");
    }
}
