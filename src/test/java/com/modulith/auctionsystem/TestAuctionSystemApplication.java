package com.modulith.auctionsystem;

import org.springframework.boot.SpringApplication;

public class TestAuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(NoteHubApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
