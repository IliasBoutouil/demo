package me.ilias.domains.loan;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Table(name = "LOAN_RATE_PARAM")
@Setter
@ToString
@NoArgsConstructor
public class LoanRateParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "RATE")
    private BigDecimal rate;
    @Column(name = "TVA")

    private BigDecimal tva;
    @Column(name = "ANNUAL_INSURANCE_RATE")
    private BigDecimal annualInsuranceRate;
    @Column(name = "LOAN_AMOUNT_MIN")

    private Long amountMin;
    @Column(name = "LOAN_AMOUNT_MAX")
    private Long amountMax;
    @Column(name = "DURATION_MIN")
    private Long durationMin;
    @Column(name = "DURATION_MAX")
    private Long durationMax;
    @Column(name = "REVENUE_MIN")
    private Long revenueMin;
    @Column(name = "REVENUE_MAX")
    private Long revenueMax;
    @Column(name = "MIN_ANNUAL_INSURANCE_FEES_AMOUNT")
    private BigDecimal minAnnualInsuranceFeesAmount;
    @ManyToOne
    @JoinColumn(name = "LOAN_TYPE_FK_ID")
    private LoanTypes loanType;
    @ManyToOne
    @JoinColumn(name = "JOB_TYPE_FK_ID")

    private JobTybe jobType;
}
