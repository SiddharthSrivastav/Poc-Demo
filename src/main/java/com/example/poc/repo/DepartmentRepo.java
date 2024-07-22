package com.example.poc.repo;

import com.example.poc.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<Departments,String>{
    @Query(value = "SELECT DISTINCT department_name FROM department_table; ",nativeQuery = true)
    List<String> allDepartments();
}
