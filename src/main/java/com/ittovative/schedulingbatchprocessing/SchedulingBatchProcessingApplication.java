package com.ittovative.schedulingbatchprocessing;

import com.ittovative.schedulingbatchprocessing.model.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

@SpringBootApplication
public class SchedulingBatchProcessingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchedulingBatchProcessingApplication.class, args);
    }


}
