# Jackpot - BE Home Assignment

## Run instructions

1. Start Kafka and Zookeeper locally (or use Docker).
   Move to docker directory and run
    ```bash
     docker-compose up -d
    ```
2. Create kafka topic
   Open a terminal inside the Kafka container and run
   ```bash
   docker exec -it kafka bash
   kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic jackpot-bets
   ```
3. Build the project:
./gradlew clean build
4. Run the service:
./gradlew bootRun

5. Import Postman collection - JackpotService.postman_collection.json 
   Or use curl commands below for testing
```bash
curl --location 'http://localhost:8080/api/jackpots' \
--header 'Content-Type: application/json' \
--data '[
        {
        "name":"Jackpot-Fixed-1",
        "initialPool": 10.0,
        "currentPool": 0.0,
        "maxPool": 100.0,
        "contributionType": "FIXED",
        "fixedContributionPercentage":0.1,
        "initialContributionPercentage":0.5,
        "contributionDecayRate": 0.1,
        "rewardType":"FIXED",
        "fixedRewardChance":0.5

    },
    {
        "name":"Jackpot-Variable-1",
        "initialPool": 10.0,
        "currentPool": 0.0,
        "maxPool": 100.0,
        "contributionType": "VARIABLE",
        "initialContributionPercentage":0.5,
        "contributionDecayRate": 0.1,
        "rewardType":"VARIABLE"

    }
]'
```
6. Test publishing a fixed bet:

```bash
curl --location 'http://localhost:8080/api/bets' \
--header 'Idempotency-key: 4bce6261-8d8e-41f4-afd2-8ddb6b7329f0' \
--header 'Content-Type: application/json' \
--data '{
    "betId":"bet-fixed-1",
    "userId": 5001,
    "jackpotId": 1,
    "betAmount":50
}'
```
7. Evaluate Bet 
```bash
curl --location --request POST 'http://localhost:8080/api/bets/bet-fixed-1/evaluate'
```

8. Test publishing a variable bet:
```bash
curl --location 'http://localhost:8080/api/bets' \
--header 'Idempotency-key: c6ee36c0-c60d-4cfd-bf1d-5396c3cfab0f' \
--header 'Content-Type: application/json' \
--data '{
"betId":"bet-variable-1",
"userId": 5001,
"jackpotId": 2,
"betAmount":100
}'
```
9. Evaluate Bet
```bash
curl --location --request POST 'http://localhost:8080/api/bets/bet-variable-1/evaluate'
```
## Notes

Service publish the event to the Kafka topic `jackpot-bets`. The consumer will pick it up and process jackpot contributions.
If kafka enabled is false in application.yml then the event is logged and bet processing method is invoked from the service class.
The database is H2 in-memory. Console available at http://localhost:8080/h2-console

## Design pattern 
Factory pattern is used for Contribution and Reward strategies. Option to add more strategies in future

## GlobalExceptionHandler
Added GlobalExceptionHandler with a custom exception BetEvaluationException. More can be added to this.

## IdempotencyKey
To validate the duplicate bet publish request

## Kafka Producer Retry
Config added in application yml

## Kafka Consumer Retry
Can be added by configuring DefaultErrorHandler in consumer config

## Unit Tests
Few unit tests are added . More can be added later for complete code coverage

## Integration Tests
Integration tests can be added using EmbeddedKafka

## Optimistic locking 
Version attribute added in entity Jackpot for optimistic locking

## Extensions

The service can be further enhanced with additional features such as:

- **Resilience**: Add retry mechanisms, circuit breakers, and fallback strategies.
- **Security**: Implement authentication, authorization, and data encryption.
- **Caching**: Introduce caching layers to improve performance and reduce database load.
- **Kafka Enhancements**: Configure producer and consumer retries, dead-letter topics (DLTs), and monitoring.
- **Testing**: Expand unit test coverage and include more integration and end-to-end tests for reliability.  