# Batch Processing Demo

This is a batch brocessing demo used for processing chunks of data from a kafka broker or a relational database and writing them in chunks in a CSV file and can be restarted if any error occurs inside the batch processing


# Prerequisites: 
- Java 21
- Maven
- Kafka
- Zookeeper
- DB (Any database that is supported inside JDBC)
- Docker (Optionally but recommended to have to just use the instances without having to install it yourself)

NOTE: there is a `docker-compose.yaml` file for both kafka ,zookeeper and postgres that are used in this demo you can just use `docker-compose up -d` then you would have Kafka, Zookeeper and a PostgreSQL database isntances running

# Running
1. Clone the repository 
    ```
    git clone https://github.com/ittovate/batch-processing.git
    ```
2. Navigate to the project directory
    ```
    cd batch-processing
    ```
3. Run the following command to start Kafka,Zookeeper and PostgreSQL instances 
   ```
   docker-compose up -d
   ```
   This command will start new instances but if you already ran it before you could use
   ```
   docker-compose start
   ```
   Note : Make sure that the instances are up and running using 
   ````
   docker ps
   ````
   If there is an instance not running correctly run it again using
   ```
   docker start {spring_batch_postgres_db} OR {spring-batch-zookeeper-demo} OR {spring-batch-kafka-demo}
   ```
   run ``docker start`` then append the name of either of the above 3 names if any of them is not shown when using ``docker ps``
4. Build and run the project
    ```
    mvn clean install spring-boot:run
    ```
### For more information on how to use this demo you can refer to [Usage](./USAGE.md).
