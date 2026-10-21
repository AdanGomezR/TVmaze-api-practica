package com.adan.tvmaze_api.show;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shows_cache")
public record ShowCacheDocument(
        @Id Long id,
        Map<String, Object> payload,
        Instant cachedAt) {
}