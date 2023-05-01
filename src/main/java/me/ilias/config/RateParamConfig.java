package me.ilias.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "grill")
public class RateParamConfig {
    private String rate;
    private String tva;
    private String annualInsuranceRate;
    private String loanAmountMin;
    private String loanAmountMax;
    private String durationMin;
    private String durationMax;
    private String revenueMin;
    private String revenueMax;
    private String minAnnualInsuranceFeesAmount;
    private String jobLabelFr;
    private String jobTypeId;
    private String loanTypeId;

}
