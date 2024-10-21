package com.avoris.prueba.search.api.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;

import com.avoris.prueba.search.api.jpa.repository.SearchJpaRepository;
import com.avoris.prueba.search.api.messaging.event.SearchEvent;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class KafkaConsumerTest extends BaseKafkaTest {

    @Mock
    private SearchJpaRepository searchRepository; // Simulamos el repositorio

    @Autowired
    private KafkaTemplate<String, SearchEvent> kafkaTemplate; // Usamos para enviar el mensaje

    @SpyBean
    private KafkaConsumer kafkaConsumer; // Inyectamos el consumidor real

    @Value("${app.kafka.topic}")
    private String topic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeMessageWithRealKafkaConsumer() throws InterruptedException {    	
        // Configuramos el objeto de prueba SearchEvent
        SearchEvent searchEvent = new SearchEvent();
        searchEvent.setHotelId("12345");
        searchEvent.setCheckIn("2023-11-15");
        searchEvent.setCheckOut("2023-11-20");
        searchEvent.setSearchId("search123");
        searchEvent.setAges(java.util.Arrays.asList(30, 25));
        
        // Envía el mensaje al Kafka embebido
        kafkaTemplate.send(topic, searchEvent);
        
        Thread.sleep(3000); // Le damos un pequeño margen para que el mensaje sea consumido
        
        // Capturamos el argumento pasado al método consumeMessage()
        ArgumentCaptor<SearchEvent> captor = forClass(SearchEvent.class);
        verify(kafkaConsumer, times(1)).consumeMessage(captor.capture());

        // Obtenemos el SearchEvent que fue recibido por consumeMessage
        SearchEvent receivedEvent = captor.getValue();

        // Comprobamos que los datos del SearchEvent recibido coinciden con los enviados
        assertEquals(searchEvent.getSearchId(), receivedEvent.getSearchId());
        assertEquals(searchEvent.getHotelId(), receivedEvent.getHotelId());
        assertEquals(searchEvent.getCheckIn(), receivedEvent.getCheckIn());
        assertEquals(searchEvent.getCheckOut(), receivedEvent.getCheckOut());
        assertEquals(searchEvent.getAges(), receivedEvent.getAges());
    }
}
