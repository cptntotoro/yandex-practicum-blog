//package ru.practicum.config;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = PostgresDynamicTest.TestConfig.class)
//public class PostgresDynamicTest {
//
//    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass");
//
//    static {
//        postgresContainer.start();
//    }
//
//    @DynamicPropertySource
//    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
//        registry.add("database.url", postgresContainer::getJdbcUrl);
//        registry.add("database.username", postgresContainer::getUsername);
//        registry.add("database.password", postgresContainer::getPassword);
//    }
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Test
//    void testConnection() throws SQLException {
//        assertNotNull(dataSource, "DataSource должен быть настроен");
//        try (Connection connection = dataSource.getConnection()) {
//            assertFalse(connection.isClosed(), "Подключение к базе данных должно быть активным");
//        }
//    }
//
//    @Configuration
//    @PropertySource("classpath:application-test.properties") // Подключаем файл свойств, если нужно
//    static class TestConfig {
//
//        @Bean
//        public DataSource dataSource(@DynamicPropertyRegistry DynamicPropertyRegistry registry) {
//            DriverManagerDataSource dataSource = new DriverManagerDataSource();
//            dataSource.setDriverClassName("org.postgresql.Driver");
//            dataSource.setUrl(registry.get("database.url", String.class));
//            dataSource.setUsername(registry.get("database.username", String.class));
//            dataSource.setPassword(registry.get("database.password", String.class));
//            return dataSource;
//        }
//    }
//}
