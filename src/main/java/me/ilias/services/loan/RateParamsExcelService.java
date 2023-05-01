package me.ilias.services.loan;

import lombok.extern.slf4j.Slf4j;
import me.ilias.config.RateParamConfig;
import me.ilias.domains.loan.JobType;
import me.ilias.domains.loan.LoanRateParam;
import me.ilias.domains.loan.LoanTypes;
import me.ilias.repositories.loan.JobTypeRepository;
import me.ilias.repositories.loan.LoanRateParamRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class RateParamsExcelService implements RateParamsService {

    public static final String STRING = "STRING";
    LoanRateParamRepository loanRateParamRepository;
    JobTypeRepository jobTypeRepository;
    RateParamConfig rateParamConfig;
    private Map<String, Integer> headerMap;

    public RateParamsExcelService(LoanRateParamRepository loanRateParamRepository, JobTypeRepository jobTypeRepository, RateParamConfig rateParamConfig) {
        this.loanRateParamRepository = loanRateParamRepository;
        this.jobTypeRepository = jobTypeRepository;
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
            extractNewJobs(sheet,rowCount);
            for (int i = 1; i < rowCount; i++) {
                LoanRateParam rateParam = createLoanRateParamFromRow(sheet.getRow(i));
                rateParams.add(rateParam);
            }
            saveExcelLocally(workbook,"grille-"+ LocalDate.now()+".xlsx");
            return loanRateParamRepository.saveAll(rateParams);
        }

    }

    private void saveExcelLocally(Workbook workbook,String path) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
        outputStream.close();
    }

    @Transactional
    public void extractNewJobs(Sheet sheet, int rowCount)
    {
        Map<String,Long> jobs= new HashMap<>();
        for (int i = 1; i < rowCount ; i++) {
            Row row = sheet.getRow(i);
            Cell jobIdCell = getCellByName(row, rateParamConfig.getJobTypeId());
           if( jobIdCell.getCellType().name().equals(STRING) && jobIdCell.getStringCellValue().equals("A créer"))
           {
               Cell labelCell = getCellByName(row, rateParamConfig.getJobLabelFr());
               String jobLabel = labelCell.getStringCellValue();
               if(!jobs.containsKey(jobLabel) )
               {
                   //todo : solve duplicated jobs labels ( id is auto increment) : by unique or db check
                   JobType savedJob = jobTypeRepository.save(JobType.builder().labelFr(jobLabel).build());
                   Long jobId = savedJob.getId();
                   jobs.put(jobLabel,jobId );
                   jobIdCell.setCellValue(jobId);
               }
               else
               {
                   jobIdCell.setCellValue(jobs.get(jobLabel));
               }
           }
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
        return cell.getCellType().name().equals(STRING) ? new BigDecimal(cell.getStringCellValue()) : BigDecimal.valueOf(cell.getNumericCellValue());
    }

    private Long getLongValue(Row row, String columnName) {
        Cell cell = getCellByName(row, columnName);
        return cell.getCellType().name().equals(STRING) ? Long.parseLong(cell.getStringCellValue()) : (long) cell.getNumericCellValue();
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
                .jobType(JobType.builder().id(getLongValue(row, rateParamConfig.getJobTypeId())).build())
                .loanType(LoanTypes.builder().id(getLongValue(row, rateParamConfig.getLoanTypeId())).build())
                .build();
    }


}
