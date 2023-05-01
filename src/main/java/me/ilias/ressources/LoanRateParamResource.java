package me.ilias.ressources;

import lombok.SneakyThrows;
import me.ilias.domains.loan.LoanRateParam;
import me.ilias.services.loan.RateParamsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController @RequestMapping("/rate")
public class LoanRateParamResource {

    RateParamsService rateParamsService;

    public LoanRateParamResource(RateParamsService rateParamsService) {
        this.rateParamsService = rateParamsService;
    }

    @PostMapping
    @SneakyThrows
    public List<LoanRateParam> uploadRates(@RequestParam("file") MultipartFile file)
    {
        return rateParamsService.loadLoanParamsFromXLS(file);
    }
}
