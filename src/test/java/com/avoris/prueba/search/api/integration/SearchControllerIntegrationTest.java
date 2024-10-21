package com.avoris.prueba.search.api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avoris.prueba.search.api.jpa.model.SearchDocument;
import com.avoris.prueba.search.api.request.SearchRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "hotel_availability_searches" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SearchControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate; // MongoTemplate to verify persistence

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mongoTemplate.getDb().drop(); // Clean MongoDB before each test
    }

    @Test
    void testSearchHotelAndPersistInMongo() throws Exception {
        SearchRequestDTO searchRequest = new SearchRequestDTO(
            "1234", "29/12/2023", "12/01/2024", Arrays.asList(12, 32, 61, 19)
        );

        // Act: Enviar la solicitud POST
        MvcResult mvcResult = mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").exists()).andReturn();
        
        // Recuperar el cuerpo de la respuesta como un String
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        // Parsear el JSON de la respuesta para extraer el searchId
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        String searchId = jsonNode.get("searchId").asText();  // Aquí extraemos el searchId

        TimeUnit.SECONDS.sleep(5); // Se espera un tiempo breve para que el consumidor Kafka procese el mensaje y lo persista en MongoDB

        // Verificar que el evento fue persistido en MongoDB
        Optional<SearchDocument> searchDocumentOptional = Optional.ofNullable(
            mongoTemplate.findById(searchId, SearchDocument.class)
        );
        assertEquals(true, searchDocumentOptional.isPresent(), "El evento no fue guardado en MongoDB.");
        if (searchDocumentOptional.isPresent()) {
            SearchDocument searchDocument = searchDocumentOptional.get();
            assertEquals(searchRequest.hotelId(), searchDocument.getHotelId());
            assertEquals(searchRequest.checkIn(), searchDocument.getCheckIn());
            assertEquals(searchRequest.checkOut(), searchDocument.getCheckOut());
            assertEquals(searchRequest.ages(), searchDocument.getAges());
        }
    }
}
