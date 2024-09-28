package com.ittovative.batchprocessing.config;

import com.ittovative.batchprocessing.model.Order;
import com.ittovative.batchprocessing.util.AppConstants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The type Batch config.
 */
@Configuration
public class BatchConfig {

    private final Logger logger = Logger.getLogger(BatchConfig.class.getName());

    /**
     * Database order processing job job.
     *
     * @param jobRepository              the job repository
     * @param platformTransactionManager the platform transaction manager
     * @param dataSource                 the data source
     * @param pagingQueryProvider        the paging query provider
     * @return the job
     */
    @Bean
    public Job databaseOrderProcessingJob(JobRepository jobRepository,
                                          PlatformTransactionManager platformTransactionManager,
                                          DataSource dataSource, PagingQueryProvider pagingQueryProvider) {
        return new JobBuilder("database-order-processing-job", jobRepository)
                .start(databaseProcessOrder(jobRepository, platformTransactionManager,
                        dataSource, pagingQueryProvider))
                .build();
    }

    /**
     * Kafka order processing job job.
     *
     * @param jobRepository               the job repository
     * @param platformTransactionManager  the platform transaction manager
     * @param defaultKafkaConsumerFactory the default kafka consumer factory
     * @return the job
     */
    @Bean
    public Job kafkaOrderProcessingJob(JobRepository jobRepository,
                                       PlatformTransactionManager platformTransactionManager,
                                       DefaultKafkaConsumerFactory<Long, Order> defaultKafkaConsumerFactory) {
        return new JobBuilder("kafka-order-processing-job", jobRepository)
                .start(kafkaProcessOrder(jobRepository, platformTransactionManager,
                        defaultKafkaConsumerFactory))
                .build();
    }

    /**
     * Database process order step.
     *
     * @param jobRepository              the job repository
     * @param platformTransactionManager the platform transaction manager
     * @param dataSource                 the data source
     * @param pagingQueryProvider        the paging query provider
     * @return the step
     */
    @Bean
    public Step databaseProcessOrder(JobRepository jobRepository,
                                     PlatformTransactionManager platformTransactionManager,
                                     DataSource dataSource,
                                     PagingQueryProvider pagingQueryProvider) {
        return new StepBuilder("database-order-processing-step", jobRepository)
                .<Order, Order>chunk(AppConstants.CHUNK_SIZE, platformTransactionManager)
                .reader(jdbcOrderItemReader(dataSource, pagingQueryProvider))
                .processor(itemProcessor())
                .writer(flatFileItemWriter())
                .build();
    }

    /**
     * Kafka process order step.
     *
     * @param jobRepository               the job repository
     * @param platformTransactionManager  the platform transaction manager
     * @param defaultKafkaConsumerFactory the default kafka consumer factory
     * @return the step
     */
    @Bean
    public Step kafkaProcessOrder(JobRepository jobRepository,
                                  PlatformTransactionManager platformTransactionManager,
                                  DefaultKafkaConsumerFactory<Long, Order> defaultKafkaConsumerFactory) {
        return new StepBuilder("kafka-order-processing-step", jobRepository)
                .<Order, Order>chunk(AppConstants.CHUNK_SIZE, platformTransactionManager)
                .reader(kafkaOrderItemReader(defaultKafkaConsumerFactory))
                .processor(itemProcessor())
                .writer(flatFileItemWriter())
                .build();
    }

    /**
     * Jdbc order item reader jdbc paging item reader.
     *
     * @param dataSource          the data source
     * @param pagingQueryProvider the paging query provider
     * @return the jdbc paging item reader
     */
    @Bean
    public JdbcPagingItemReader<Order> jdbcOrderItemReader(DataSource dataSource,
                                                           PagingQueryProvider pagingQueryProvider) {
        return new JdbcPagingItemReaderBuilder<Order>()
                .saveState(true)
                .name("jdbc-item-reader")
                .dataSource(dataSource)
                .queryProvider(pagingQueryProvider)
                .pageSize(AppConstants.PAGE_SIZE)
                .rowMapper((resultSet, rowNum) -> {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    return new Order(id, name, description);
                })
                .build();

    }

    /**
     * Paging query provider factory bean sql paging query provider factory bean.
     *
     * @param dataSource the data source
     * @return the sql paging query provider factory bean
     */
    @Bean
    public SqlPagingQueryProviderFactoryBean pagingQueryProviderFactoryBean(DataSource dataSource) {
        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("select *");
        factoryBean.setFromClause("from orders");
        factoryBean.setSortKey("id");
        return factoryBean;
    }

    /**
     * Kafka order item reader kafka item reader.
     *
     * @param kafkaConsumerFactory the kafka consumer factory
     * @return the kafka item reader
     */
    @Bean
    public KafkaItemReader<Long, Order> kafkaOrderItemReader(
            DefaultKafkaConsumerFactory<Long, Order> kafkaConsumerFactory) {
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.putAll(kafkaConsumerFactory.getConfigurationProperties());
        return new KafkaItemReaderBuilder<Long, Order>()
                .name("orders-kafka-item-reader")
                .partitions(0)
                .saveState(true)
                .topic("orders")
                .consumerProperties(kafkaConsumerProperties)
                .partitionOffsets(new HashMap<>())
                .build();
    }

    /**
     * Item processor item processor.
     *
     * @return the item processor
     */
    @Bean
    public ItemProcessor<Order, Order> itemProcessor() {
        return item -> {
            logger.info("Order: {" + item.name().toLowerCase(Locale.ROOT) + "} is being processed!");
            Thread.sleep(AppConstants.THREAD_SLEEP_TIME_MS); // simulating real processing time
            return item;
        };
    }

    /**
     * Flat file item writer flat file item writer.
     *
     * @return the flat file item writer
     */
    @Bean
    public FlatFileItemWriter<Order> flatFileItemWriter() {
        return new FlatFileItemWriterBuilder<Order>()
                .name("orders-item-writer")
                .append(true)
                .saveState(true)
                .delimited()
                .names("id", "name", "description")
                .resource(new FileSystemResource("new_orders.csv"))
                .build();
    }
}
