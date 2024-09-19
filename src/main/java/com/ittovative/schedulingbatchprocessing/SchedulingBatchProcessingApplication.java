package com.ittovative.schedulingbatchprocessing;

import com.ittovative.schedulingbatchprocessing.model.Person;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class SchedulingBatchProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingBatchProcessingApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner commandLineRunner(KafkaTemplate<Long, Person> kafkaTemplate) {
        return (args) -> {
            for (int i = 0; i < 200; i++) {
                Person person = new Person();
                person.setName("Mohanad#"+i);
                person.setAge(i);
                kafkaTemplate.send("dummy-orders",person);
            }
        };
    }*/

}
