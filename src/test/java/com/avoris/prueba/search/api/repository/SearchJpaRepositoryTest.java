package com.avoris.prueba.search.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.avoris.prueba.search.api.jpa.model.SearchDocument;
import com.avoris.prueba.search.api.jpa.repository.SearchJpaRepository;

@DataMongoTest
class SearchJpaRepositoryTest {

    @Autowired
    private SearchJpaRepository searchRepository;

    private SearchDocument searchDocument;

    @BeforeEach
    void setUp() {
        // Inicializa un documento de ejemplo
        searchDocument = new SearchDocument();
        searchDocument.setSearchId("12345");
        searchDocument.setHotelId("7854");
        searchDocument.setCheckIn("12/01/2023");
        searchDocument.setCheckOut("20/01/2023");
        searchDocument.setAges(Arrays.asList(23, 3, 19, 87));
        
        // Guardar el documento en la base de datos embebida
        searchRepository.save(searchDocument);
    }

    @Test
    void testFindBySearchIdSuccess() {
        // Llama al método real del repositorio
        Optional<SearchDocument> result = searchRepository.findBySearchId("12345");

        // Verifica que el resultado esté presente y los campos coincidan
        assertTrue(result.isPresent());
        assertEquals("12345", result.get().getSearchId());
        assertEquals("7854", result.get().getHotelId());
    }

    @Test
    void testFindBySearchIdNotFound() {
        // Busca un searchId inexistente
        Optional<SearchDocument> result = searchRepository.findBySearchId("99999");

        // Verifica que no se encuentre el documento
        assertFalse(result.isPresent());
    }

    @Test
    void testCountByHotelIdAndCheckInAndCheckOutAndAgesSuccess() {
        // Llama al método count
        long count = searchRepository.countByCustomQuery(
                "7854", "12/01/2023", "20/01/2023", Arrays.asList(23, 3, 19, 87)
        );

        // Verifica que el conteo sea 1
        assertEquals(1, count);
    }

    @Test
    void testCountByHotelIdAndCheckInAndCheckOutAndAgesNoMatch() {
        // Llama al método count con parámetros que no coinciden
        long count = searchRepository.countByCustomQuery(
                "9999", "01/01/2023", "15/01/2023", Arrays.asList(10, 20, 30)
        );

        // Verifica que el conteo sea 0
        assertEquals(0, count);
    }
    
    @Test
    void testSaveSuccess() {
        // Guardar el documento en la base de datos embebida
        SearchDocument savedDocument = searchRepository.save(searchDocument);

        // Verificar que el documento se ha guardado correctamente
        assertEquals(searchDocument.getSearchId(), savedDocument.getSearchId());
        assertEquals(searchDocument.getHotelId(), savedDocument.getHotelId());
        assertEquals(searchDocument.getCheckIn(), savedDocument.getCheckIn());
        assertEquals(searchDocument.getCheckOut(), savedDocument.getCheckOut());
        assertEquals(searchDocument.getAges(), savedDocument.getAges());

        // Verificar que el documento existe en la base de datos
        assertTrue(searchRepository.findById(savedDocument.getSearchId()).isPresent());
    }
}
