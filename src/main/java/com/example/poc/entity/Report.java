package com.example.poc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("reports_table")
public class Report {
    @Transient
    public static final String SEQUENCE_NAME = "report_id_seq";
    @Id
    private Long reportId;
    private List<String> department;
    private String description;
    private String assignedTo;
    private LocalDateTime dateOfCreation;

}
