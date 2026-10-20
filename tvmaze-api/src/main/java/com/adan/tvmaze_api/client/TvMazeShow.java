package com.adan.tvmaze_api.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeShow(
        Long id,
        String name,
        Channel network,
        Channel webChannel,
        String summary,
        List<String> genres) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Channel(String name) {
    }
}