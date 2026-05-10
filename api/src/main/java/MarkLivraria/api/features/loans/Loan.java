package MarkLivraria.api.features.loans;

import MarkLivraria.api.features.books.Book;
import MarkLivraria.api.features.users.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table (name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "books_id")
    private Book book;

    private LocalDateTime loanDate;
    private LocalDateTime expectedReturnDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}