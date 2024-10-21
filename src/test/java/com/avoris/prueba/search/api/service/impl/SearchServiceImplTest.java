package com.avoris.prueba.search.api.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.avoris.prueba.search.api.jpa.adapter.SearchRepositoryAdapter;
import com.avoris.prueba.search.api.messaging.EventProducer;
import com.avoris.prueba.search.api.messaging.event.SearchEvent;
import com.avoris.prueba.search.api.model.Search;

class SearchServiceImplTest {
	
	@InjectMocks
    private SearchServiceImpl searchService;

	@Mock
    private SearchRepositoryAdapter searchRepositoryAdapter;  
    
	@Mock
	private EventProducer eventProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testSearch() {
        // Mock de los datos de entrada (SearchRequest)
        Search search = new Search(
            "testId", "hotel-123", "2024-10-15", "2024-10-20", 
            Arrays.asList(25, 30)
        );

        // Mock del comportamiento del repositorio (no existe el searchId generado)
        when(searchRepositoryAdapter.existsById(anyString())).thenReturn(false);

        // Ejecutar el método que se está probando
        String searchId = searchService.search(search);

        // Verificar que se ha invocado al repositorio para comprobar la existencia del searchId
        verify(searchRepositoryAdapter, times(1)).existsById(anyString());

        // Verificar que se envió correctamente el evento a Kafka
        verify(eventProducer, times(1)).sendMessage(any(SearchEvent.class));
        
        // Usamos argThat para comprobar que las propiedades del evento enviado a Kafka sean correctas
        verify(eventProducer, times(1)).sendMessage(argThat(event -> 
            event.getHotelId().equals(search.getHotelId()) &&
            event.getCheckIn().equals(search.getCheckIn()) &&
            event.getCheckOut().equals(search.getCheckOut()) &&
            event.getAges().equals(search.getAges()) &&
            event.getSearchId().equals(searchId)
        ));
    }

    @Test
    void testSearchGeneratesNewIdIfExists() {
        // Mock del comportamiento del repositorio: el primer searchId ya existe
        when(searchRepositoryAdapter.existsById(anyString())).thenReturn(true).thenReturn(false);

        // Mock de los datos de entrada (SearchRequest)
        Search search = new Search(
            "testId", "hotel-123", "2024-10-15", "2024-10-20", 
            Arrays.asList(25, 30)
        );

        // Ejecutar el método
        searchService.search(search);

        // Verificar que se llamó dos veces al método existsById (porque el primer ID ya existía)
        verify(searchRepositoryAdapter, times(2)).existsById(anyString());

        // Verificar que se invocó al productor de Kafka
        verify(eventProducer, times(1)).sendMessage(any(SearchEvent.class));
    }

}
