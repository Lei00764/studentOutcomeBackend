package com.example.studentoutcomebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.studentoutcomebackend.mapper"})
public class StudentOutcomeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentOutcomeBackendApplication.class, args);
    }

}
