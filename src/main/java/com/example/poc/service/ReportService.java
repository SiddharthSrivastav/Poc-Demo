package com.example.poc.service;

import com.example.poc.dto.ReportRequestDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ReportService {

    String saveReport(ReportRequestDto requestDto);
    void downloadReports(List<Integer> reportIds, String dateOfCreation, HttpServletResponse response);
}
