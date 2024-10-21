package com.avoris.prueba.search.api.kafka;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "hotel_availability_searches" }, 
    brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}, controlledShutdown = true)
public abstract class BaseKafkaTest {
}
