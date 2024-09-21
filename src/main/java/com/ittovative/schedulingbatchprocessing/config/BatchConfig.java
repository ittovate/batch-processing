package com.ittovative.schedulingbatchprocessing.config;

import com.ittovative.schedulingbatchprocessing.model.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
public class BatchConfig {

    private final Logger logger = Logger.getLogger(BatchConfig.class.getName());

    @Bean
    public JobLauncher asyncJobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher taskExecutorJobLauncher = new TaskExecutorJobLauncher();
        taskExecutorJobLauncher.setJobRepository(jobRepository);
        taskExecutorJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        taskExecutorJobLauncher.afterPropertiesSet();
        return taskExecutorJobLauncher;
    }

    @Bean("orderProcessingJob")
    public Job orderProcessingJob(JobRepository jobRepository,
                        PlatformTransactionManager platformTransactionManager,
                        DefaultKafkaConsumerFactory<Long,Order> kafkaConsumerFactory) {
        return new JobBuilder("order-processing-job",jobRepository)
                .start(processKafkaToFileStep(jobRepository,platformTransactionManager,kafkaConsumerFactory))
                .build();
    }

    @Bean
    public Step processKafkaToFileStep(JobRepository jobRepository,
                                       PlatformTransactionManager platformTransactionManager,
                                       DefaultKafkaConsumerFactory<Long,Order> kafkaConsumerFactory){
        return new StepBuilder("order-processing-step",jobRepository)
                .allowStartIfComplete(true)
                .<Order, Order> chunk(5,platformTransactionManager)
                .reader(kafkaOrderItemReader(kafkaConsumerFactory))
                .processor(itemProcessor())
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public KafkaItemReader<Long,Order> kafkaOrderItemReader(
            DefaultKafkaConsumerFactory<Long,Order> kafkaConsumerFactory){
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.putAll(kafkaConsumerFactory.getConfigurationProperties());
        return new KafkaItemReaderBuilder<Long,Order>()
                .name("orders-kafka-item-reader")
                .partitions(0)
                .saveState(true)
                .topic("orders")
                .consumerProperties(kafkaConsumerProperties)
                .partitionOffsets(new HashMap<>())
                .build();
    }

    @Bean
    public ItemProcessor<Order,Order> itemProcessor(){
        return item -> {
            logger.info("Order: {" + item.name().toLowerCase(Locale.ROOT) + "} is being processed!");
            Thread.sleep(1000); // simulating real processing time
            return item;
        };
    }

    @Bean
    public FlatFileItemWriter<Order> flatFileItemWriter(){
        return new FlatFileItemWriterBuilder<Order>()
                .name("csv-order-item-writer")
                .append(true)
                .saveState(true)
                .delimited()
                .names("name","description")
                .resource(new FileSystemResource("new_orders.csv"))
                .build();
    }
}
