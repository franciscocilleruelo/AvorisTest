package com.avoris.prueba.search.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.avoris.prueba.search.api.mapper.SearchMapper;
import com.avoris.prueba.search.api.request.SearchRequestDTO;
import com.avoris.prueba.search.api.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SearchController.class)
@AutoConfigureMockMvc
class SearchControllerTest {

    @MockBean
    private SearchService searchService;
    
    @MockBean
    private SearchMapper searchMapper;
    
    @Autowired
    private ObjectMapper objectMapper; 

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSearchHotelsSuccess() throws Exception {
        SearchRequestDTO searchRequest = new SearchRequestDTO(
                "1234", "29/12/2023", "12/01/2024", Arrays.asList(12, 32, 61, 19)
        );

        when(searchService.search(any())).thenReturn("789456");

        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").isNotEmpty())
                .andExpect(jsonPath("$.searchId").value("789456"));
    }

    @Test
    void testSearchHotelsInvalidPayload() throws Exception {
        // Envío de una request con un payload inválido
        String invalidPayload = "{ \"invalidField\": \"value\" }";

        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
    
    @Test
    void testSearchHotelsInvalidDate() throws Exception {
    	SearchRequestDTO searchRequest = new SearchRequestDTO(
                "1234", "29/56/2023", "48/01/2024", Arrays.asList(12, 32, 61, 19)
        );
        
        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
    
    @Test
    void testSearchHotelsInvalidDateFormat() throws Exception {
    	SearchRequestDTO searchRequest = new SearchRequestDTO(
                "1234", "29/12", "wrong", Arrays.asList(12, 32, 61, 19)
        );
        
        mockMvc.perform(post("/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

}

