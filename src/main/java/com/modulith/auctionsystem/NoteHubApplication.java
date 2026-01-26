package com.modulith.auctionsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NoteHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteHubApplication.class, args);
    }

}
