package com.adan.tvmaze_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceAccessException.class)
    public ProblemDetail handleConnectionFailure(
            ResourceAccessException exception) {

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.GATEWAY_TIMEOUT,
                "No fue posible obtener una respuesta de TVMaze a tiempo."
        );
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ProblemDetail handleUpstreamResponse(
            RestClientResponseException exception) {

        HttpStatus status = exception.getStatusCode().value() == 429
                ? HttpStatus.SERVICE_UNAVAILABLE
                : HttpStatus.BAD_GATEWAY;

        return ProblemDetail.forStatusAndDetail(
                status,
                "TVMaze no pudo completar la consulta."
        );
    }

    @ExceptionHandler(RestClientException.class)
    public ProblemDetail handleClientFailure(
            RestClientException exception) {

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_GATEWAY,
                "La respuesta de TVMaze no pudo procesarse."
        );
    }
}