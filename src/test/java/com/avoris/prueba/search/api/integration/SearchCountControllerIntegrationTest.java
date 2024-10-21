package com.avoris.prueba.search.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.avoris.prueba.search.api.jpa.model.SearchDocument;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "hotel_availability_searches" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SearchCountControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mongoTemplate.getDb().drop(); // Limpiar MongoDB antes de cada test
    }

    @Test
    void testGetSearchCountWithSimilarSearches() throws Exception {
        // Insertar múltiples documentos similares en MongoDB
        SearchDocument searchDocument1 = new SearchDocument(
            "1234", "29/12/2023", "12/01/2024", Arrays.asList(12, 32, 61, 19), "test-search-id-1"
        );
        SearchDocument searchDocument2 = new SearchDocument(
            "1234", "29/12/2023", "12/01/2024", Arrays.asList(12, 32, 61, 19), "test-search-id-2"
        );
        SearchDocument searchDocument3 = new SearchDocument(
            "1234", "29/12/2023", "12/01/2024", Arrays.asList(12, 32, 61, 19), "test-search-id-3"
        );
        mongoTemplate.save(searchDocument1);
        mongoTemplate.save(searchDocument2);
        mongoTemplate.save(searchDocument3);

        // Simular una llamada GET al endpoint /count con el searchId del documento 1
        mockMvc.perform(get("/count")
                .param("searchId", "test-search-id-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("test-search-id-1"))
                .andExpect(jsonPath("$.search.hotelId").value("1234"))
                .andExpect(jsonPath("$.search.checkIn").value("29/12/2023"))
                .andExpect(jsonPath("$.search.checkOut").value("12/01/2024"))
                .andExpect(jsonPath("$.search.ages[0]").value(12))
                .andExpect(jsonPath("$.search.ages[1]").value(32))
                .andExpect(jsonPath("$.search.ages[2]").value(61))
                .andExpect(jsonPath("$.search.ages[3]").value(19))
                .andExpect(jsonPath("$.count").value(3));
    }

    @Test
    void testGetSearchCountNotFound() throws Exception {
        // Simular una llamada GET al endpoint /count con un searchId inexistente
        mockMvc.perform(get("/count")
                .param("searchId", "non-existent-search-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

