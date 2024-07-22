package com.example.poc.dto;


import lombok.Data;


@Data
public class ReportRequestDto {
    private String department;
    private String description;
    private String assignedTo;
}
