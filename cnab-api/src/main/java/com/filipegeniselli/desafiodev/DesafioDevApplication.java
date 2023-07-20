package com.filipegeniselli.desafiodev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
@ConfigurationPropertiesScan
public class DesafioDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesafioDevApplication.class, args);
    }

}
