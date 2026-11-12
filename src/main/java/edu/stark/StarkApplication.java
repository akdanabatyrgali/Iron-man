package edu.stark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Entry point for the browser UI, JSON REST API and database. */
@SpringBootApplication
public class StarkApplication {
    public static void main(String[] args) {
        SpringApplication.run(StarkApplication.class, args);
    }
}
