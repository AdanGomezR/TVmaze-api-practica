package com.adan.tvmaze_api.show;

import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import com.adan.tvmaze_api.client.TvMazeSearchResult;
import com.adan.tvmaze_api.client.TvMazeShow;
import com.adan.tvmaze_api.comment.CommentDocument;
import com.adan.tvmaze_api.comment.CommentRepository;
import com.adan.tvmaze_api.comment.CommentResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adan.tvmaze_api.client.TvMazeClient;
import org.springframework.stereotype.Service;

@Service
public class ShowService {
	
	private final CommentRepository commentRepository;

    private final TvMazeClient tvMazeClient;
    public ShowService(
            TvMazeClient tvMazeClient,
            ShowCacheRepository showCacheRepository,
            CommentRepository commentRepository) {

        this.tvMazeClient = tvMazeClient;
        this.showCacheRepository = showCacheRepository;
        this.commentRepository = commentRepository;
    }

    public List<ShowSearchResponse> search(String query) {
        List<TvMazeSearchResult> results =
                tvMazeClient.search(query.trim());

        if (results.isEmpty()) {
            return List.of();
        }

        List<Long> showIds = results.stream()
                .map(result -> result.show().id())
                .distinct()
                .toList();

        List<CommentDocument> comments =
                commentRepository.findByShowIdInOrderByCreatedAtAscIdAsc(
                        showIds
                );

        Map<Long, List<CommentResponse>> commentsByShow =
                comments.stream()
                        .collect(Collectors.groupingBy(
                                CommentDocument::showId,
                                Collectors.mapping(
                                        document -> new CommentResponse(
                                                document.comment(),
                                                document.rating()
                                        ),
                                        Collectors.toList()
                                )
                        ));

        return results.stream()
                .map(result -> toSearchResponse(
                        result.show(),
                        commentsByShow.getOrDefault(
                                result.show().id(),
                                List.of()
                        )
                ))
                .toList();
    }

    private ShowSearchResponse toSearchResponse(
            TvMazeShow show,
            List<CommentResponse> comments) {

        return new ShowSearchResponse(
                show.id(),
                show.name(),
                resolveChannel(show),
                show.summary(),
                show.genres() == null ? List.of() : show.genres(),
                comments
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
    
    public Map<String, Object> getShowWithComments(long showId) {
        Map<String, Object> show = getShow(showId);

        List<CommentResponse> comments = commentRepository
                .findByShowIdOrderByCreatedAtAscIdAsc(showId)
                .stream()
                .map(document -> new CommentResponse(
                        document.comment(),
                        document.rating()
                ))
                .toList();

        Map<String, Object> response = new LinkedHashMap<>(show);
        response.put("comments", comments);

        return response;
    }
}