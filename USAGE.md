# To use this demo you can 
- Add dummy orders to kafka or a db
- Start batch processing all elements in kafka/db *in case of failure it can be restarted to the last successful commit*

## Adding orders to kafka
```
POST http://localhost:8080/api/v1/kafka
Content-Type: application/json

{
"description": "example",
"name": "example"
}

```

## Adding orders to db

```
POST http://localhost:8080/api/v1/kafka
Content-Type: application/json

{
"description": "example",
"name": "example"
}
```

## Start batch processing in ``orders`` topic in kafka

``
POST http://localhost:8080/api/v1/batch-kafka
``

## Start batch processing in ``orders`` topic in kafka

``
POST http://localhost:8080/api/v1/batch-db
``
