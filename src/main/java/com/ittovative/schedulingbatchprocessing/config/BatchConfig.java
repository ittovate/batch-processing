package com.ittovative.schedulingbatchprocessing.config;

import com.ittovative.schedulingbatchprocessing.model.Person;
import org.apache.kafka.common.TopicPartition;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Properties;

@Configuration
public class BatchConfig {

    @Bean
    public KafkaItemReader<Long,Person> kafkaItemReader(DefaultKafkaConsumerFactory<Long,Person> kafkaConsumerFactory){
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.putAll(kafkaConsumerFactory.getConfigurationProperties());
        return new KafkaItemReaderBuilder<Long,Person>()
                .name("kafka-item-reader")
                .partitions(0)
                .saveState(true)
                .topic("dummy-orders")
                .consumerProperties(kafkaConsumerProperties)
                /*.partitionOffsets(new HashMap<>())*/
                .build();
    }

    @Bean
    public Job dummyJob(JobRepository jobRepository,
                        PlatformTransactionManager platformTransactionManager,
                        DefaultKafkaConsumerFactory<Long,Person> defaultKafkaConsumerFactory){
        return new JobBuilder("dummy-job",jobRepository)
                .start(dummyStep(jobRepository,platformTransactionManager,defaultKafkaConsumerFactory))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step dummyStep(JobRepository jobRepository,
                          PlatformTransactionManager platformTransactionManager,
                          DefaultKafkaConsumerFactory<Long,Person> defaultKafkaConsumerFactory){
        return new StepBuilder("dummy-step",jobRepository)
                .<Person,Person>chunk(10,platformTransactionManager)
                .reader(kafkaItemReader(defaultKafkaConsumerFactory))
                .processor(itemProcessor())
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<Person,Person> itemProcessor(){
        return item -> {
            System.out.println(item + " being processed!");
            Thread.sleep(1000);
            return item;
        };
    }

    @Bean
    public FlatFileItemWriter<Person> flatFileItemWriter(){
        return new FlatFileItemWriterBuilder<Person>()
                .name("csv-item-writer")
                .append(true)
                .saveState(true)
                .delimited()
                .names("name","age")
                .resource(new FileSystemResource("new_persons.csv"))
                .build();
    }
}
