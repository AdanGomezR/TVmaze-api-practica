package com.adan.tvmaze_api.show;

import java.util.List;

public record ShowSearchResponse(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres) {
}