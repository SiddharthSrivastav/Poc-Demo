package com.example.poc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="department_table")
public class Departments {
    @Id
    @Column(name="department_id")
    private String departmentId;
    @Column(name = "department_name")
    private String departmentName;
}
