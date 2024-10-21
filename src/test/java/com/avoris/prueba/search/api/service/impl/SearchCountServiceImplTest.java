package com.avoris.prueba.search.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.avoris.prueba.search.api.jpa.adapter.SearchRepositoryAdapter;
import com.avoris.prueba.search.api.jpa.model.SearchCount;
import com.avoris.prueba.search.api.model.Search;

/**
 * Unit tests for {@link SearchCountServiceImpl}.
 */
class SearchCountServiceImplTest {

    @Mock
    private SearchRepositoryAdapter searchRepositoryAdapter;  

    @InjectMocks
    private SearchCountServiceImpl searchCountService;  

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  
    }

    @Test
    void testGetSearchCountResponseBySearchIdSuccess() {
        // Datos de prueba
        String searchId = "search123";
        Search search = new Search(searchId ,
            "hotel123",        
            "15/10/2024",      
            "20/10/2024",      
            Arrays.asList(30, 25)                 
        );

        // Configuramos el comportamiento del mock para devolver el documento de búsqueda
        when(searchRepositoryAdapter.findBySearchId(searchId)).thenReturn(Optional.of(search));

        // Simulamos el conteo de búsquedas similares
        when(searchRepositoryAdapter.countSearches(any(String.class), any(String.class), any(String.class), any())).thenReturn(5L);

        // Llamamos al método del servicio
        Optional<SearchCount> responseOptional = searchCountService.getSearchCountResponseBySearchId(searchId);

        // Verificamos que la respuesta está presente y que los datos son correctos
        assertTrue(responseOptional.isPresent());
        SearchCount response = responseOptional.get();
        assertEquals(searchId, response.getSearchId());
        assertEquals(5L, response.getCount());

        // Verificamos los detalles de la búsqueda
        SearchCount.SearchDetails searchDetails = response.getSearch();
        assertEquals(search.getHotelId(), searchDetails.getHotelId());
        assertEquals(search.getCheckIn(), searchDetails.getCheckIn());
        assertEquals(search.getCheckOut(), searchDetails.getCheckOut());
        assertEquals(search.getAges(), searchDetails.getAges());
    }

    @Test
    void testGetSearchCountResponseBySearchIdNotFound() {
        // Configuramos el mock para devolver un Optional vacío cuando no se encuentra el searchId
        String searchId = "nonExistentSearchId";
        when(searchRepositoryAdapter.findBySearchId(searchId)).thenReturn(Optional.empty());

        // Llamamos al método del servicio
        Optional<SearchCount> responseOptional = searchCountService.getSearchCountResponseBySearchId(searchId);

        // Verificamos que la respuesta está vacía
        assertTrue(responseOptional.isEmpty());
    }
}
