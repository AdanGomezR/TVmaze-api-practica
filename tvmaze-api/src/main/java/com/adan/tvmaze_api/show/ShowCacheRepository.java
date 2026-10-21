package com.adan.tvmaze_api.show;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowCacheRepository
        extends MongoRepository<ShowCacheDocument, Long> {
}