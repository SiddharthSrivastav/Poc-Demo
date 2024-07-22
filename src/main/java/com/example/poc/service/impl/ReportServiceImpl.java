package com.example.poc.service.impl;

import com.example.poc.dbsequence.SequenceGenerator;
import com.example.poc.dto.ReportRequestDto;
import com.example.poc.entity.Report;
import com.example.poc.exception.GenericException;
import com.example.poc.repo.DepartmentRepo;
import com.example.poc.repo.ReportRepo;
import com.example.poc.service.ReportService;
import com.example.poc.util.ExcelGenerator;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Validated
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ReportRepo reportRepo;
    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private SequenceGenerator sequenceGenerator;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String saveReport(ReportRequestDto requestDto) {
        if (StringUtils.isBlank(requestDto.getDepartment())) {
            throw new GenericException(HttpStatus.NOT_ACCEPTABLE, "Department Name can't be blank");
        }
        Report report = Report.builder().assignedTo(requestDto.getAssignedTo()).description(requestDto.getDescription()).department(requestDto.getDepartment().toUpperCase()).reportId(sequenceGenerator.generateSequence(Report.SEQUENCE_NAME)).build();
        log.info("generated report entity: ", report);
        List<String> departments = departmentRepo.allDepartments();
        log.info("Checking if department {} exists in {}", report.getDepartment().toUpperCase(), departments);
        if (departments.contains(report.getDepartment().toUpperCase())) {
            report.setDateOfCreation(LocalDateTime.now());
            reportRepo.save(report);
            log.info("Report saved successfully");
        } else {
            log.error("Error validating department:");
            throw new GenericException(HttpStatus.NOT_ACCEPTABLE, "Incorrect department");
        }
        return report.getReportId().toString();

    }

    @Override
    public void downloadReports(List<Integer> reportIds, String dateOfCreation, HttpServletResponse response) {
        List<Report> reports = reportRepo.findAll();
        Criteria criteria = new Criteria();
        log.info("All the reports are fetched successfully with details :", reports);
        if (Objects.nonNull(reportIds)) {
            criteria = criteria.and("reportId").in(reportIds);
        }
        if (StringUtils.isNotBlank(dateOfCreation)) {
            LocalDate date = LocalDate.parse(dateOfCreation);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            criteria = criteria.and("dateOfCreation").gte(startOfDay).lte(endOfDay);
        }
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria));
        try {
            AggregationResults<Report> results = mongoTemplate.aggregate(aggregation, "reports_table", Report.class);
            log.info("Filtered reports based on input request: ", results);
            ExcelGenerator generator = new ExcelGenerator(results.getMappedResults());
            generator.generateExcelFile(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new GenericException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download report");
        }
    }
}
