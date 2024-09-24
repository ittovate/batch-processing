package com.ittovative.schedulingbatchprocessing;

import com.ittovative.schedulingbatchprocessing.model.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class SchedulingBatchProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingBatchProcessingApplication.class, args);
    }

    // uncomment to insert dummy orders to kafka at the app's startup

    /*@Bean
    public CommandLineRunner commandLineRunner(KafkaTemplate<Long, Order> kafkaTemplate) {
        return (args) -> {
            for (int i = 0; i < 500; i++) {
                Order person = new Order(i,"order#"+i,"dummy description");
                kafkaTemplate.send("orders",person);
            }
        };
    }*/

}
