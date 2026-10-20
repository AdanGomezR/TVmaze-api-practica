package com.adan.tvmaze_api.exception;

public class ShowNotFoundException extends RuntimeException {

    public ShowNotFoundException(long showId) {
        super("No existe un show con el ID " + showId + ".");
    }
}