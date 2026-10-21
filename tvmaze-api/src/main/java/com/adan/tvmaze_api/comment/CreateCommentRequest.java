package com.adan.tvmaze_api.comment;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(

        @JsonProperty("show_id")
        @NotNull(message = "show_id es obligatorio.")
        @Positive(message = "show_id debe ser mayor que cero.")
        Long showId,

        @NotBlank(message = "comment no debe estar vacío.")
        @Size(
                max = 2000,
                message = "comment debe tener máximo 2000 caracteres."
        )
        String comment,

        @NotNull(message = "rating es obligatorio.")
        @DecimalMin(
                value = "0",
                message = "rating debe ser mayor o igual a 0."
        )
        @DecimalMax(
                value = "5",
                message = "rating debe ser menor o igual a 5."
        )
        BigDecimal rating
) {
}