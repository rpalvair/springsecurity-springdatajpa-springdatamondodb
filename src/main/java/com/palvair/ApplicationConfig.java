package com.palvair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by widdy on 20/12/2015.
 */
@SpringBootApplication
public class ApplicationConfig {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApplicationConfig.class);
        //springApplication.setAdditionalProfiles("inMemory");
        springApplication.run(args);
    }
}
