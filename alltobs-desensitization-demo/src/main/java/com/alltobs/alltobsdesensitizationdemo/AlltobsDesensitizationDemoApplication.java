package com.alltobs.alltobsdesensitizationdemo;

import com.alltobs.desensitization.annotation.EnableAllbsDesensitization;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAllbsDesensitization
public class AlltobsDesensitizationDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlltobsDesensitizationDemoApplication.class, args);
    }

}
