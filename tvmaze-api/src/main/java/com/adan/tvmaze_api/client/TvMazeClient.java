package com.adan.tvmaze_api.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TvMazeClient {

    private final RestClient restClient;

    public TvMazeClient(RestClient tvMazeRestClient) {
        this.restClient = tvMazeRestClient;
    }
/*
 * Se crea metodo para llamar al endpoint /search/shows?q={query} de la api de tvmaze
 */
    public List<TvMazeSearchResult> search(String query) {
        List<TvMazeSearchResult> results = restClient.get()
                .uri("/search/shows?q={query}", query)
                .retrieve()
                .body(new ParameterizedTypeReference<
                        List<TvMazeSearchResult>>() {});

        if (results == null) {
            throw new IllegalStateException(
                    "TVMaze returned an empty response body");
        }

        return results;
    }
}