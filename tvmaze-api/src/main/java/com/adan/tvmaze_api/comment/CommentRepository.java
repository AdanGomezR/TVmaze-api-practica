package com.adan.tvmaze_api.comment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository
        extends MongoRepository<CommentDocument, String> {

    List<CommentDocument> findByShowIdInOrderByCreatedAtAscIdAsc(
            Collection<Long> showIds);
    
    List<CommentDocument> findByShowIdOrderByCreatedAtAscIdAsc(
            Long showId);
}