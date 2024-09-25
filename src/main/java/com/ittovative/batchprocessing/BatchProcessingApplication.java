package com.ittovative.batchprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Batch processing application.
 */
@SpringBootApplication
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class BatchProcessingApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String... args) {
        SpringApplication.run(BatchProcessingApplication.class, args);
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
