package com.adan.tvmaze_api.show;

import java.util.List;

import com.adan.tvmaze_api.client.TvMazeClient;
import com.adan.tvmaze_api.client.TvMazeShow;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

    private final TvMazeClient tvMazeClient;

    public ShowService(TvMazeClient tvMazeClient) {
        this.tvMazeClient = tvMazeClient;
    }

    public List<ShowSearchResponse> search(String query) {
        return tvMazeClient.search(query.trim()).stream()
                .map(result -> toSearchResponse(result.show()))
                .toList();
    }

    private ShowSearchResponse toSearchResponse(TvMazeShow show) {
        return new ShowSearchResponse(
                show.id(),
                show.name(),
                resolveChannel(show),
                show.summary(),
                show.genres() == null ? List.of() : show.genres()
        );
    }

    private String resolveChannel(TvMazeShow show) {
        if (show.network() != null
                && show.network().name() != null) {
            return show.network().name();
        }

        return show.webChannel() == null
                ? null
                : show.webChannel().name();
    }
}