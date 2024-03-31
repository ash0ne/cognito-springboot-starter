package com.ashone.cognitostarter

import com.ashone.cognitostarter.integration.BaseIntegrationTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
/**
 * This class contains integration tests for the Cognito Spring Boot Starter application.
 *
 */
class CognitoSpringBootStarterApplicationTests : BaseIntegrationTest() {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @LocalServerPort
    private var port: Int = 0

    /**
     * Test method to verify if the Spring application starts successfully in a local environment.
     * It sends an HTTP GET request to the /actuator/health endpoint and asserts the response.
     */
    @Test
    fun contextLoads() {
        val entity: ResponseEntity<String> =
            restTemplate.getForEntity("http://localhost:${port}/actuator/health", String::class.java)
        assertEquals(HttpStatus.OK, entity.statusCode, "Health check failed")
        assertTrue(entity.body!!.contains("UP"), "Application not running")
    }

}
