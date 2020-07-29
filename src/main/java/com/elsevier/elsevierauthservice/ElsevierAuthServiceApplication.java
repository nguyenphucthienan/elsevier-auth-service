package com.elsevier.elsevierauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ElsevierAuthServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElsevierAuthServiceApplication.class, args);
  }
}
