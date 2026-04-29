package MarkLivraria.api.features.transactions;

import MarkLivraria.api.features.books.BookRepository;
import MarkLivraria.api.features.books.BookService;
import MarkLivraria.api.features.books.Physical;
import MarkLivraria.api.features.loans.Loan;
import MarkLivraria.api.features.loans.LoanRepository;
import MarkLivraria.api.features.loans.LoanStatus;
import MarkLivraria.api.features.purchases.Purchase;
import MarkLivraria.api.features.purchases.PurchaseRepository;
import MarkLivraria.api.features.users.User;
import MarkLivraria.api.features.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final PurchaseRepository purchaseRepository;
    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void executePurchase(String userEmail, Long bookId) {

        // 1. Buscamos quem está comprando e o que está sendo comprado
        var user = (User) userRepository.findByLogin(userEmail);
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        // 2. Regra de Negócio: Verificação de Estoque (Apenas para livros físicos)
        if (book instanceof Physical physical) {
            if (physical.getStockQuantity() <= 0) {
                throw new IllegalStateException("Estoque esgotado para o books físico selecionado.");
            }
            // Retira 1 do estoque
            physical.setStockQuantity(physical.getStockQuantity() - 1);
            bookRepository.save(physical);
        }

        // 3. Efetiva a purchases (Ebooks passam direto pra cá)
        var purchase = new Purchase();
        purchase.setUser(user);
        purchase.setBook(book);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setAmountPaid(book.getPrice());

        purchaseRepository.save(purchase);
    }

    @Transactional
    public void executeLoan(String userEmail, Long bookId) {

        var user = (User) userRepository.findByLogin(userEmail);
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        // Regra de Negócio: Não emprestamos Ebooks, apenas Físicos
        if (!(book instanceof Physical physical)) {
            throw new IllegalArgumentException("Apenas livros físicos podem ser emprestados.");
        }

        // Regra de Negócio: Tem na prateleira?
        if (physical.getStockQuantity() <= 0) {
            throw new IllegalStateException("Todos os exemplares deste books já estão emprestados ou vendidos.");
        }

        // Retira 1 da prateleira
        physical.setStockQuantity(physical.getStockQuantity() - 1);
        bookRepository.save(physical);

        // Gera o contrato de empréstimo (Dura 14 dias)
        var loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setExpectedReturnDate(LocalDateTime.now().plusDays(14));
        loan.setStatus(LoanStatus.ATIVO);

        loanRepository.save(loan);
    }
}