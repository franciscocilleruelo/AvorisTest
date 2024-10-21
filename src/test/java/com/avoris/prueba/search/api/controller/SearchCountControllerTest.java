package com.avoris.prueba.search.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.avoris.prueba.search.api.jpa.model.SearchCount;
import com.avoris.prueba.search.api.jpa.model.SearchCount.SearchDetails;
import com.avoris.prueba.search.api.mapper.SearchMapper;
import com.avoris.prueba.search.api.service.SearchCountService;

@WebMvcTest(controllers = SearchCountController.class, 
	includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SearchMapper.class)) 
@AutoConfigureMockMvc
class SearchCountControllerTest {

    @Autowired
    private MockMvc mockMvc;  

    @MockBean
    private SearchCountService searchCountService;  

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  
    }

    @Test
    void testGetSearchCount_Success() throws Exception {
        // Datos de prueba
        String searchId = "search123";
        
        // Creamos el objeto SearchDetails simulado
        SearchDetails searchDetails = new SearchCount.SearchDetails(
            "hotel123",    // Hotel ID
            "15/10/2024",  // CheckIn date
            "20/10/2024",  // CheckOut date
            Arrays.asList(30, 25)  // Lista de edades
        );
        
        // Creamos el mock de la respuesta que contiene SearchDetails
        SearchCount mockResponse = new SearchCount(searchId, searchDetails, 5L);

        // Configuramos el comportamiento del servicio para devolver datos
        when(searchCountService.getSearchCountResponseBySearchId(searchId)).thenReturn(Optional.of(mockResponse));

     // Simulamos la solicitud HTTP GET y verificamos el resultado
        mockMvc.perform(get("/count")
                .param("searchId", searchId)  // Parámetro de consulta
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Verificamos que el estado es 200 OK
                .andExpect(jsonPath("$.searchId").value("search123"))
                .andExpect(jsonPath("$.search.hotelId").value("hotel123"))
                .andExpect(jsonPath("$.search.checkIn").value("15/10/2024"))
                .andExpect(jsonPath("$.search.checkOut").value("20/10/2024"))
                .andExpect(jsonPath("$.count").value(5));
    }

    @Test
    void testGetSearchCount_NotFound() throws Exception {
        // Datos de prueba
        String searchId = "nonExistentSearchId";

        // Configuramos el mock para devolver un Optional.empty() cuando no existe el searchId
        when(searchCountService.getSearchCountResponseBySearchId(searchId)).thenReturn(Optional.empty());

        // Simulamos la solicitud HTTP GET y verificamos el resultado
        mockMvc.perform(get("/count")
                .param("searchId", searchId)  // Parámetro de consulta
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  // Verificamos que el estado es 404 Not Found
    }
}
