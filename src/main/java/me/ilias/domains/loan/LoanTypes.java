package me.ilias.domains.loan;

import lombok.*;
import me.ilias.enums.LoanType;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Table(name = "LOAN_TYPES")
@Setter
@ToString
@NoArgsConstructor
public class LoanTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "LOAN_TYPE")
    @Enumerated(EnumType.STRING)
    private LoanType loanType;
}
