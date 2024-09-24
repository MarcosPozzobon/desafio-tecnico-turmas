package com.marcos.desenvolvimento.desafio_tecnico.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public DataSourceConfig(
            @Value("${spring.datasource.url}") String URL,
            @Value("${spring.datasource.username}") String USER,
            @Value("${spring.datasource.password}") String PASSWORD) {
        this.URL = URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl(URL);
        hikariConfig.setUsername(USER);
        hikariConfig.setPassword(PASSWORD);

        hikariConfig.setIdleTimeout(10000);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setValidationTimeout(1000);
        hikariConfig.setMinimumIdle(5);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
