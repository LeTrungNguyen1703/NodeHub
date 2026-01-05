package com.modulith.auctionsystem;

import org.springframework.boot.SpringApplication;

public class TestAuctionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuctionSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
