package ru.alexander.projects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TaskManagingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagingApplication.class, args);
    }
}