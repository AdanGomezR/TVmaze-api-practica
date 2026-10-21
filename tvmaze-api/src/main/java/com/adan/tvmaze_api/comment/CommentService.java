package com.adan.tvmaze_api.comment;

import java.time.Instant;

import com.adan.tvmaze_api.show.ShowService;

import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ShowService showService;

    public CommentService(
            CommentRepository commentRepository,
            ShowService showService) {

        this.commentRepository = commentRepository;
        this.showService = showService;
    }

    public void create(CreateCommentRequest request) {
        // Comprueba que el show exista utilizando la caché actual.
        showService.getShow(request.showId());

        CommentDocument document = new CommentDocument(
                null,
                request.showId(),
                request.comment(),
                request.rating(),
                Instant.now()
        );

        commentRepository.insert(document);
    }
}