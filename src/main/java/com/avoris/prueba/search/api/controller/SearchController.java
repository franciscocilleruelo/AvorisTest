package com.avoris.prueba.search.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avoris.prueba.search.api.mapper.SearchMapper;
import com.avoris.prueba.search.api.request.SearchRequestDTO;
import com.avoris.prueba.search.api.response.SearchResponseDTO;
import com.avoris.prueba.search.api.service.SearchService;

import jakarta.validation.Valid;

/**
 * REST controller for handling hotel search requests.
 * <p>
 * This controller exposes an endpoint at <code>/search</code> to handle POST requests 
 * for saving hotel search criteria and returning a search ID.
 * </p>
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;
    private final SearchMapper searchMapper;

    /**
     * Constructor for {@link SearchController}, injecting the {@link SearchService} and {@link SearchMapper}.
     * <p>
     * The constructor initializes the service responsible for handling the business logic of searches
     * and the mapper for converting between DTOs and domain objects.
     * </p>
     * 
     * @param searchService the service responsible for handling search logic.
     * @param searchMapper  the mapper used to convert between {@link SearchRequestDTO} and domain objects.
     */
    public SearchController(SearchService searchService, SearchMapper searchMapper) {
        this.searchService = searchService;
        this.searchMapper = searchMapper;
    }

    /**
     * Handles POST requests to save the search based on the provided {@link SearchRequestDTO}.
     * <p>
     * This method takes a search request payload, validates it, and processes it via the {@link SearchService}.
     * The response contains the search ID wrapped in a {@link SearchResponseDTO}.
     * </p>
     * 
     * @param searchRequest the search criteria for finding hotels, provided in the request body.
     * @return a {@link ResponseEntity} containing the {@link SearchResponseDTO} with the search ID.
     */
    @PostMapping
    public ResponseEntity<SearchResponseDTO> searchHotels(@Valid @RequestBody SearchRequestDTO searchRequest) {
        // Convert the DTO to a domain object and perform the search
        String searchId = searchService.search(searchMapper.toDomain(searchRequest));

        // Create the response DTO with the search ID and return a 200 OK response
        SearchResponseDTO response = new SearchResponseDTO(searchId);
        return ResponseEntity.ok(response);
    }
}
