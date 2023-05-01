package me.ilias.domains.loan;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Table(name = "JOB_TYPE")
@Setter
@NoArgsConstructor
@ToString
public class JobTybe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Column(name="label_fr")
    private String labelFr;
}
