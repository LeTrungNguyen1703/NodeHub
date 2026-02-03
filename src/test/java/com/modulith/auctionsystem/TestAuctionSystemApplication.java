package com.modulith.auctionsystem;

import org.springframework.boot.SpringApplication;

public class TestAuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(NodeHubApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
