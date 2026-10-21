package com.adan.tvmaze_api.comment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository
        extends MongoRepository<CommentDocument, String> {
}