package com.example.bolasdejairo.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@ConditionalOnProperty(prefix = "app.db.cloud-sql", name = "enabled", havingValue = "true")
public class CloudSqlConnectionFactoryConfig {

    @Bean
    ConnectionFactory cloudSqlConnectionFactory(DatabaseProperties databaseProperties) {
        String instanceConnectionName = databaseProperties.getCloudSql().getInstanceConnectionName();
        if (!StringUtils.hasText(instanceConnectionName)) {
            throw new IllegalStateException(
                    "INSTANCE_CONNECTION_NAME is required when app.db.cloud-sql.enabled=true"
            );
        }

        ConnectionFactory delegate = ConnectionFactories.get(
                ConnectionFactoryOptions.builder()
                        .option(DRIVER, "gcp")
                        .option(PROTOCOL, "postgres")
                        .option(HOST, instanceConnectionName)
                        .option(DATABASE, databaseProperties.getName())
                        .option(USER, databaseProperties.getUsername())
                        .option(PASSWORD, databaseProperties.getPassword())
                        .build()
        );

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(delegate)
                .initialSize(databaseProperties.getPool().getInitialSize())
                .maxSize(databaseProperties.getPool().getMaxSize())
                .maxIdleTime(databaseProperties.getPool().getMaxIdleTime())
                .validationQuery("SELECT 1")
                .build();

        return new ConnectionPool(poolConfiguration);
    }
}
