package com.ashone.cognitostarter.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate

/**
 * Configuration class used to set up the config for tests.
 *
 * All the beans in this class should only load on the `test` profile.
 */
@Configuration
class TestConfig {

    /**
     * RestTemplate bean that is to be used in the integration tests to make HTTP calls.
     *
     * @param restTemplateBuilder The Rest Template Builder
     * @return RestTemplate
     */
    @Bean
    @Profile("test")
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return restTemplateBuilder.build()
    }
}
