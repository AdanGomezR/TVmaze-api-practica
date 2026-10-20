package com.adan.tvmaze_api.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class TvMazeConfig {

    @Bean
    public RestClient tvMazeRestClient(
            RestClient.Builder builder,
            @Value("${tvmaze.base-url}") String baseUrl) {

    	/*
    	 * Se agrega una configuracion de timeut para prevenir que nos quedemos colgados si falla el servicio
    	 */
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(Duration.ofSeconds(5));

        return builder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("User-Agent", "tvmaze-middleware/1.0")
                .build();
    }
}