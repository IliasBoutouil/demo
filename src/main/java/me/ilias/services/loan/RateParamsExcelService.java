package me.ilias.services.loan;

import lombok.extern.slf4j.Slf4j;
import me.ilias.config.RateParamConfig;
import me.ilias.domains.loan.JobTybe;
import me.ilias.domains.loan.LoanRateParam;
import me.ilias.domains.loan.LoanTypes;
import me.ilias.repositories.loan.LoanRateParamRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class RateParamsExcelService implements RateParamsService {

    LoanRateParamRepository loanRateParamRepository;
    RateParamConfig rateParamConfig;
    private Map<String, Integer> headerMap;

    public RateParamsExcelService(LoanRateParamRepository loanRateParamRepository, RateParamConfig rateParamConfig) {
        this.loanRateParamRepository = loanRateParamRepository;
        this.rateParamConfig = rateParamConfig;
    }

    @Override
    @Transactional
    public void truncateRateParams() {
        log.info("truncating loan rate table");
        loanRateParamRepository.deleteAll();
        loanRateParamRepository.resetIdentity();
    }
    @Override
    @Transactional
    public List<LoanRateParam> loadLoanParamsFromXLS(MultipartFile file) throws IOException {
        log.info("started loading from file {}", file.getOriginalFilename());
        truncateRateParams();
        List<LoanRateParam> rateParams = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            extractExcelHeaderMap(sheet);
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum()+1;
            for (int i = 1; i < rowCount; i++) {
                LoanRateParam rateParam = createLoanRateParamFromRow(sheet.getRow(i));
                rateParams.add(rateParam);
            }
            return loanRateParamRepository.saveAll(rateParams);
        }

    }

    private void extractExcelHeaderMap(Sheet sheet) {
        Row header = sheet.getRow(0);
        Iterator<Cell> cellIterator = header.cellIterator();
        headerMap = new HashMap<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        log.info("extracted {} headers : {} from {}", header.getLastCellNum() - header.getFirstCellNum(), headerMap.keySet(),sheet.getSheetName());
    }

    private Cell getCellByName(Row row, String columnName) {
        int columnIndex = headerMap.get(columnName);
        return row.getCell(columnIndex);
    }

    private BigDecimal getDecimalValue(Row row, String columnName) {
        Cell cell = getCellByName(row, columnName);
        return cell.getCellType().name().equals("STRING") ? new BigDecimal(cell.getStringCellValue()) : BigDecimal.valueOf(cell.getNumericCellValue());
    }

    private Long getLongValue(Row row, String columnName) {
        Cell cell = getCellByName(row, columnName);
        return cell.getCellType().name().equals("STRING") ? Long.parseLong(cell.getStringCellValue()) : (long) cell.getNumericCellValue();
    }
    private LoanRateParam createLoanRateParamFromRow(Row row) {
        return LoanRateParam.builder()
                .revenueMin(getLongValue(row, rateParamConfig.getRevenueMin()))
                .revenueMax(getLongValue(row, rateParamConfig.getRevenueMax()))
                .annualInsuranceRate(getDecimalValue(row, rateParamConfig.getAnnualInsuranceRate()))
                .durationMin(getLongValue(row, rateParamConfig.getDurationMin()))
                .durationMax(getLongValue(row, rateParamConfig.getDurationMax()))
                .minAnnualInsuranceFeesAmount(getDecimalValue(row, rateParamConfig.getMinAnnualInsuranceFeesAmount()))
                .rate(getDecimalValue(row, rateParamConfig.getRate()))
                .amountMin(getLongValue(row, rateParamConfig.getLoanAmountMin()))
                .amountMax(getLongValue(row, rateParamConfig.getLoanAmountMax()))
                .tva(getDecimalValue(row, rateParamConfig.getTva()))
                .jobType(JobTybe.builder().id(getLongValue(row, rateParamConfig.getJobTypeId())).build())
                .loanType(LoanTypes.builder().id(getLongValue(row, rateParamConfig.getLoanTypeId())).build())
                .build();
    }


}
