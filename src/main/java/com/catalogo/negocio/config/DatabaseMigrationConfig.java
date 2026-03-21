package com.catalogo.negocio.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class DatabaseMigrationConfig {

    @Bean
    public CommandLineRunner relaxProductCategoryConstraints(JdbcTemplate jdbcTemplate) {
        return args -> {
            List<String> constraintNames = jdbcTemplate.query(
                    """
                    SELECT con.conname
                    FROM pg_constraint con
                    JOIN pg_class rel ON rel.oid = con.conrelid
                    WHERE rel.relname = 'products'
                      AND con.contype = 'c'
                      AND pg_get_constraintdef(con.oid) ILIKE '%category%'
                    """,
                    (rs, rowNum) -> rs.getString("conname"));

            for (String constraintName : constraintNames) {
                jdbcTemplate.execute("ALTER TABLE products DROP CONSTRAINT IF EXISTS " + constraintName);
            }
        };
    }
}
