package com.example.bolasdejairo.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseInitializationConfig {

    @Bean
    ConnectionFactoryInitializer connectionFactoryInitializer(
            ConnectionFactory connectionFactory,
            @Value("${app.db.init.enabled:true}") boolean databaseInitializationEnabled
    ) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        if (databaseInitializationEnabled) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                    new ClassPathResource("schema.sql"),
                    new ClassPathResource("data.sql")
            );
            initializer.setDatabasePopulator(populator);
        }

        return initializer;
    }
}
