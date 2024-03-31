package com.ashone.cognitostarter.integration

import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.PostgreSQLContainer

/**
 * BaseIntegrationTest is a base class for integration tests in the com.ashone.cognitostarter.integration package.
 * It initializes a PostgreSQL container using Testcontainers library for testing purposes and configures
 * Spring Boot properties for to connect to the test instance of PostgreSQL.
 *
 */
@SpringJUnitConfig
@SpringBootTest
@TestPropertySource(
    properties = [
        "spring.datasource.username=test",
        "spring.datasource.password=test"
    ]
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BaseIntegrationTest {
    /**
     * Lazily initializes a PostgreSQL container using Testcontainers library.
     * The PostgreSQL container is configured with the latest PostgreSQL image and sets up the database,
     * username, and password accordingly.
     */
    private final val postgreSQLContainer: PostgreSQLContainer<Nothing> by lazy {
        PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("cognito-starter-test")
            withUsername("test")
            withPassword("test")
        }
    }

    /**
     * Initializes the PostgreSQL container and starts it.
     * Sets the 'spring.datasource.url' property to the dynamically mapped port of the PostgreSQL container.
     */
    init {
        postgreSQLContainer.start()
        System.setProperty("spring.datasource.url", getDataSourceUrl())
    }

    // The method to build JDBC URL for the PostgreSQL test container.
    // Need to do this as PostgreSQL container gets mapped to a dynamic port free port each time.
    private fun getDataSourceUrl(): String {
        return "jdbc:postgresql://localhost:${postgreSQLContainer.getMappedPort(5432)}/cognito-starter-test"
    }
}
