package me.ilias.services.loan;

import me.ilias.domains.loan.LoanRateParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RateParamsService {
    void truncateRateParams();
    List<LoanRateParam> loadLoanParamsFromXLS(MultipartFile file) throws IOException;

}
