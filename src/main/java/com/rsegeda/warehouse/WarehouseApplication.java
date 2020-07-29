package com.rsegeda.warehouse;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class WarehouseApplication implements CommandLineRunner {

  @Override
  public void run(String... args) {
    log.info("Service started");
  }

  public static void main(String[] args) {
    log.info("Starting the service");
    SpringApplication.run(WarehouseApplication.class, args);
  }
}
