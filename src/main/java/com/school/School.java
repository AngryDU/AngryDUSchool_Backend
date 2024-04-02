package com.school;

import com.school.security.RsaKeyProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaKeyProperty.class})
@SpringBootApplication
public class School {

    public static void main(String[] args) {
        SpringApplication.run(School.class, args);
    }

}
