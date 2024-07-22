package com.example.poc.repo;

import com.example.poc.entity.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepo extends MongoRepository<Report,String> {



}
