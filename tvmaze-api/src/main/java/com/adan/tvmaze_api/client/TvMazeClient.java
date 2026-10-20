package com.adan.tvmaze_api.client;

import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;

import com.adan.tvmaze_api.exception.ShowNotFoundException;

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
    
    public Map<String, Object> getShow(long showId) {
        Map<String, Object> show = restClient.get()
                .uri("/shows/{show_id}", showId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        (request, response) -> {
                            throw new ShowNotFoundException(showId);
                        }
                )
                .body(new ParameterizedTypeReference<
                        Map<String, Object>>() {});

        if (show == null || show.isEmpty()) {
            throw new RestClientException(
                    "TVMaze devolvió una respuesta vacía para el show " + showId
            );
        }

        return show;
    }
}