package com.avoris.prueba.search.api.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import com.avoris.prueba.search.api.messaging.event.SearchEvent;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class KafkaProducerTest extends BaseKafkaTest {
    
	@Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;
    
    @Value("${app.kafka.topic}") 
    private String topic;

    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        // Inicialización para sincronizar la recepción de mensajes
        latch = new CountDownLatch(1); 
    }

    @Test
    void testSendMessage() throws InterruptedException {
    	// Envía un mensaje al Kafka embebido
        SearchEvent searchEvent = new SearchEvent();
        searchEvent.setHotelId("154542");
        searchEvent.setCheckIn("12/01/2023");
        searchEvent.setCheckOut("10/02/2023");
        searchEvent.setSearchId("asda70ewrndasjke3241");        

        // Configura el consumidor
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        
        // Configura el deserializador de JSON para SearchEvent
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, SearchEvent.class.getName());
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        
        ConsumerFactory<String, SearchEvent> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps,
            new ErrorHandlingDeserializer<>(new JsonDeserializer<>()), // Deserializador para clave
            new ErrorHandlingDeserializer<>(new JsonDeserializer<>(SearchEvent.class)) // Deserializador para valor
        );
        
        ConcurrentKafkaListenerContainerFactory<String, SearchEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        // Crear y configurar el contenedor de mensajes
        MessageListenerContainer container = factory.createContainer(topic);
        
        // Usamos AtomicReference para poder recuperar el valor del evento recibido
        AtomicReference<SearchEvent> eventReceivedRef = new AtomicReference<>();
        
        container.setupMessageListener((MessageListener<String, SearchEvent>) result -> {
            eventReceivedRef.set(result.value());
            
            // Notificar la recepción del mensaje
            latch.countDown(); 
        });

        container.start();

        kafkaProducer.sendMessage(searchEvent);

        // Esperar hasta que el mensaje sea consumido (con un tiempo máximo de espera)
        boolean messageConsumed = latch.await(10, TimeUnit.SECONDS);
        assertEquals(true, messageConsumed, "El mensaje no fue consumido a tiempo.");
        if (messageConsumed) {
        	SearchEvent eventReceived = eventReceivedRef.get();
        	// Verificar que los datos recibidos son los esperados
            assertEquals(searchEvent.getHotelId(), eventReceived.getHotelId());
            assertEquals(searchEvent.getCheckIn(), eventReceived.getCheckIn());
            assertEquals(searchEvent.getCheckOut(), eventReceived.getCheckOut());
            assertEquals(searchEvent.getSearchId(), eventReceived.getSearchId());
            assertEquals(searchEvent.getAges(), eventReceived.getAges());
        }

        container.stop();
    }
    
}

