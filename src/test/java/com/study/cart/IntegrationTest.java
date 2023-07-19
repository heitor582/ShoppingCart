package com.study.cart;

import com.study.cart.configuration.ObjectMapperConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(SQLCleanUpExtension.class)
@Import(ObjectMapperConfig.class)
public abstract class IntegrationTest {
    @Autowired
    protected MockMvc http;

    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres")
                .withDatabaseName("cart")
                .withUsername("cart")
                .withPassword("cart")
                .withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    private static void setProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        registry.add("spring.flyway.enabled", () -> false);

    }
}