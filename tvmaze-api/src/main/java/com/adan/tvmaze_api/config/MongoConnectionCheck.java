package com.adan.tvmaze_api.config;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("mongo-check")
public class MongoConnectionCheck {

    private static final Logger log =
            LoggerFactory.getLogger(MongoConnectionCheck.class);

    @Bean
    public ApplicationRunner checkMongoConnection(
            MongoTemplate mongoTemplate) {

        return args -> {
            Document result = mongoTemplate.executeCommand(
                    new Document("ping", 1)
            );

            log.info(
                    "Conexión a MongoDB verificada. Base: {}, resultado: {}",
                    mongoTemplate.getDb().getName(),
                    result.get("ok")
            );
        };
    }
}