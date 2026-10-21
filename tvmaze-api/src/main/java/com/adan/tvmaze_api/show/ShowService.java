package com.adan.tvmaze_api.show;

import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adan.tvmaze_api.client.TvMazeClient;
import com.adan.tvmaze_api.client.TvMazeShow;
import org.springframework.stereotype.Service;

@Service
public class ShowService {

    private final TvMazeClient tvMazeClient;
    public ShowService(
            TvMazeClient tvMazeClient,
            ShowCacheRepository showCacheRepository) {

        this.tvMazeClient = tvMazeClient;
        this.showCacheRepository = showCacheRepository;
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
    
    public Map<String, Object> getShow(long showId) {
        return showCacheRepository.findById(showId)
                .map(cachedShow -> {
                    log.debug("Show {} obtenido desde MongoDB", showId);

                    return cachedShow.payload();
                })
                .orElseGet(() -> fetchAndCacheShow(showId));
    }
    private static final Logger log =
            LoggerFactory.getLogger(ShowService.class);
    private final ShowCacheRepository showCacheRepository;
    
    private Map<String, Object> fetchAndCacheShow(long showId) {
        log.debug(
                "Show {} no encontrado en caché. Consultando TVMaze",
                showId
        );

        Map<String, Object> show = tvMazeClient.getShow(showId);

        ShowCacheDocument document = new ShowCacheDocument(
                showId,
                show,
                Instant.now()
        );

        showCacheRepository.save(document);

        log.debug("Show {} guardado en MongoDB", showId);

        return show;
    }
}