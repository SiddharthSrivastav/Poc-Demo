package com.example.poc.dto;


import lombok.Data;

import java.util.List;


@Data
public class ReportRequestDto {
    private List<String> department;
    private String description;
    private String assignedTo;
}
