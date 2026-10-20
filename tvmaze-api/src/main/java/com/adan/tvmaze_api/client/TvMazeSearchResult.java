package com.adan.tvmaze_api.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeSearchResult(TvMazeShow show) {
}