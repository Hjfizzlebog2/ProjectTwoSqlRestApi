package com.example.projecttwosqlrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjectTwoSqlRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectTwoSqlRestApiApplication.class, args);
    }

}
