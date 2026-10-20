package com.adan.tvmaze_api.show;


import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }
/*
 * Metodo para llamar al endpoint de nuestra api
 */
    @GetMapping("/search")
    public List<ShowSearchResponse> search(
            @RequestParam("search_query")
            @NotBlank
            @Size(max = 200)
            String searchQuery) {

        return showService.search(searchQuery);
    }
}