package com.classroomassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class ClassroomassistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClassroomassistantApplication.class, args);
    }

}
