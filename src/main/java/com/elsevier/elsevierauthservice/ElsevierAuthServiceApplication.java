package com.elsevier.elsevierauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableEurekaClient
@SpringBootApplication
@EntityScan(basePackageClasses = {
        ElsevierAuthServiceApplication.class,
        Jsr310JpaConverters.class
})
public class ElsevierAuthServiceApplication {

  @PostConstruct
  void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  public static void main(String[] args) {
    SpringApplication.run(ElsevierAuthServiceApplication.class, args);
  }
}
