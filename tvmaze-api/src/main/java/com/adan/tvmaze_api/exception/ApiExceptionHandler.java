package com.adan.tvmaze_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
    
    @ExceptionHandler(ShowNotFoundException.class)
    public ProblemDetail handleShowNotFound(
            ShowNotFoundException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problem.setTitle("Show no encontrado");

        return problem;
    }
    
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidation(
            HandlerMethodValidationException exception) {

        // Una validación fallida del valor de retorno es un error del servidor.
        if (exception.isForReturnValue()) {
            return ProblemDetail.forStatusAndDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "La respuesta generada no cumple las validaciones."
            );
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Los parámetros no cumplen las validaciones. "
                        + "show_id debe ser mayor que cero; "
                        + "search_query debe contener texto y tener máximo "
                        + "200 caracteres."
        );

        problem.setTitle("Parámetros inválidos");

        return problem;
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(
            MethodArgumentTypeMismatchException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El parámetro '" + exception.getName()
                        + "' tiene un formato incorrecto."
        );

        problem.setTitle("Formato de parámetro inválido");

        return problem;
    }
}