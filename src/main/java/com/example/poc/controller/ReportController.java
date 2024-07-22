package com.example.poc.controller;

import com.example.poc.dto.ReportRequestDto;
import com.example.poc.exception.GenericException;
import com.example.poc.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Report", description = "Report APIs")
public class ReportController {
    @Autowired
    ReportService reportService;


    @Operation(
            summary = "Retrieve a Report by Id",
            description = "Get a report detail.",
            tags = {"reports", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    @PostMapping("/createReport")
    public ResponseEntity<String> newReport(@RequestBody ReportRequestDto requestDto) {
        log.info("Report Request:", requestDto);
        try {
            String reportId = reportService.saveReport(requestDto);
            return new ResponseEntity<>("Report generated successfully with report ID:: " + reportId, HttpStatus.OK);
        } catch (GenericException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Unable to create Report");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/download")
    public void exportIntoExcelFile(HttpServletResponse response, @RequestParam(required = false) List<Integer> reportId,
                                    @RequestParam(required = false) String dateOfCreation) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Report" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        reportService.downloadReports(reportId, dateOfCreation, response);

    }
}
