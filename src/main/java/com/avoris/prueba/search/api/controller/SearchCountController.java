package com.avoris.prueba.search.api.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avoris.prueba.search.api.jpa.model.SearchCount;
import com.avoris.prueba.search.api.mapper.SearchMapper;
import com.avoris.prueba.search.api.response.SearchCountResponseDTO;
import com.avoris.prueba.search.api.service.SearchCountService;

/**
 * REST controller for handling search count requests.
 * <p>
 * This controller provides an endpoint to retrieve the search count details for a specific search ID.
 * It exposes the <code>/count</code> endpoint to handle GET requests.
 * </p>
 */
@RestController
@RequestMapping("/count")
public class SearchCountController {

    private final SearchCountService searchService;
    private final SearchMapper searchMapper;

    /**
     * Constructor for {@link SearchCountController}, injecting the {@link SearchCountService} and {@link SearchMapper}.
     * <p>
     * The constructor initializes the service responsible for retrieving search count information 
     * and the mapper for converting between domain objects and DTOs.
     * </p>
     * 
     * @param searchService the service responsible for handling search count logic.
     * @param searchMapper  the mapper used to convert between domain objects and {@link SearchCountResponseDTO}.
     */
    public SearchCountController(SearchCountService searchService, SearchMapper searchMapper) {
        this.searchService = searchService;
        this.searchMapper = searchMapper;
    }

    /**
     * Handles GET requests to retrieve the search count and details for a given search ID.
     * <p>
     * This method takes a search ID as a request parameter and looks up the search count and its details
     * by delegating the logic to the {@link SearchCountService}. If the search is found, a {@link SearchCountResponseDTO}
     * is returned with a 200 OK status; if not, a 404 Not Found status is returned.
     * </p>
     * 
     * @param searchId the unique ID of the search to retrieve the count for.
     * @return a {@link ResponseEntity} containing the {@link SearchCountResponseDTO} if found, or a 404 Not Found status if not.
     */
    @GetMapping
    public ResponseEntity<SearchCountResponseDTO> getSearchCount(@RequestParam String searchId) {
    	Optional<SearchCount> searchOpt = searchService.getSearchCountResponseBySearchId(searchId);
    	if (searchOpt.isEmpty()) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    	SearchCountResponseDTO response = searchMapper.toDTO(searchOpt.get());
    	return ResponseEntity.ok(response);
    }
}
