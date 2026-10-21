package com.adan.tvmaze_api.comment;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "comments")
public record CommentDocument(
        @Id String id,
        Long showId,
        String comment,

        @Field(targetType = FieldType.DECIMAL128)
        BigDecimal rating,

        Instant createdAt
) {
}