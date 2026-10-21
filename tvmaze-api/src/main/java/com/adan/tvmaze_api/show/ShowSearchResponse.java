package com.adan.tvmaze_api.show;

import java.util.List;

import com.adan.tvmaze_api.comment.CommentResponse;

public record ShowSearchResponse(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres,
        List<CommentResponse> comments) {
}