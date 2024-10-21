package com.avoris.prueba.search.api.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.avoris.prueba.search.api.messaging.event.SearchEvent;

/**
 * Configuration class for Kafka.
 * <p>
 * This class sets up the Kafka producer and consumer factories, 
 * Kafka templates, and listener container factories for handling {@link SearchEvent}.
 * </p>
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.listener.concurrency}")
    private int concurrency;
    
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    
    /**
     * Default constructor for {@link KafkaConfig}.
     * This class does not require any custom initialization.
     */
    public KafkaConfig() {
        // No custom initialization required
    }
    
    /**
     * Creates a {@link KafkaTemplate} for producing messages to Kafka.
     * <p>
     * The template is configured to use a JSON serializer for serializing the 
     * {@link SearchEvent} objects.
     * </p>
     * 
     * @return a KafkaTemplate configured to send {@link SearchEvent} messages.
     */
    @Bean
    KafkaTemplate<String, SearchEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Configures the producer factory to produce Kafka messages with {@link SearchEvent}.
     * <p>
     * This method sets up the producer configuration properties, including
     * serialization of keys and values, batch size, and buffer memory.
     * </p>
     * 
     * @return a {@link ProducerFactory} for producing messages.
     */
    @Bean
    ProducerFactory<String, SearchEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); // JSON serializer
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);  // Low latency
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 32 * 1024);  // Batch size
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 64 * 1024 * 1024);  // Buffer memory
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    /**
     * Configures the consumer factory to consume Kafka messages with {@link SearchEvent}.
     * <p>
     * This method sets up the consumer configuration, including deserialization
     * of keys and values, group ID, and how offsets are handled.
     * </p>
     * 
     * @return a {@link ConsumerFactory} for consuming messages.
     */
    @Bean
    ConsumerFactory<String, SearchEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");        
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SearchEvent.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class); // JSON deserializer
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // Read from the beginning
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(SearchEvent.class));
    }
    
    /**
     * Creates a {@link ConcurrentKafkaListenerContainerFactory} for handling Kafka listeners.
     * <p>
     * This factory enables concurrent message consumption by configuring multiple
     * consumers to handle messages in parallel.
     * </p>
     * 
     * @param consumerFactory the consumer factory for {@link SearchEvent}.
     * @return a KafkaListenerContainerFactory for managing message listeners.
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, SearchEvent> kafkaListenerContainerFactory(ConsumerFactory<String, SearchEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, SearchEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency); // Ensures high performance with concurrent consumers
        return factory;
    }
    
}
