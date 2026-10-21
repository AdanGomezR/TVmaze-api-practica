package com.adan.tvmaze_api.comment;

import java.math.BigDecimal;

public record CommentResponse(
        String comment,
        BigDecimal rating) {
}