# Batch Processing Demo

This is a batch brocessing demo used for processing chunks of data from a kafka broker or a relational database and can be restarted if any error occurs inside the batch processing


# Prerequisites: 
- Java 21
- Maven
- Kafka
- Zookeeper
- DB (Any database that is supported inside JDBC)

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
3. Build and run the project
    ```
    mvn clean install spring-boot:run
    ```
