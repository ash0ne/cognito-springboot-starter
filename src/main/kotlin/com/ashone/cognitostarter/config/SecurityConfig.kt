package com.ashone.cognitostarter.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * Configuration class for security settings, including CORS config and HTTP filterChain for Cognito authentication.
 */
@Configuration
class SecurityConfig {
    @Value("\${spring.security.oauth2.client.provider.cognito.jwk-set-uri}")
    lateinit var jwksUrl: String

    @Value("\${cognito.issuer-uri}")
    lateinit var issuer: String

    @Value("\${cognito.provider}")
    lateinit var provider: String

    @Value("\${spring.security.oauth2.client.provider.cognito.user-name-attribute}")
    lateinit var userNameAttribute: String

    @Value("\${cognito.roles-attribute}")
    lateinit var rolesAttribute: String

    @Value("\${cors.origins}")
    lateinit var allowedOrigins: String

    @Value("\${cors.headers}")
    lateinit var allowedHeaders: String

    @Value("\${cors.methods}")
    lateinit var allowedMethods: String

    /**
     * This method configures a SecurityFilterChain bean to handle security configurations
     * when the 'cognito.client-behaviour-enabled' property is set to 'false' or not present.
     * It disables CSRF protection, enables default CORS configuration, and adds a custom
     * CognitoTokenAuthenticationFilter to the filter chain for authenticating requests.
     *
     * @param http The HttpSecurity object used for configuring security settings.
     * @return A SecurityFilterChain object configured with CSRF protection disabled,
     *         CORS configuration enabled, and a custom CognitoTokenAuthenticationFilter added to
     *         authenticate requests.
     */
    @Bean
    @ConditionalOnProperty(name = ["cognito.client-behaviour-enabled"], havingValue = "false", matchIfMissing = true)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .cors(Customizer.withDefaults<CorsConfigurer<HttpSecurity>>())
            .securityMatcher("/api/**")
            .addFilterAfter(
                CognitoTokenAuthenticationFilter(
                    jwksUrl, issuer, provider,
                    userNameAttribute, rolesAttribute
                ), BasicAuthenticationFilter::class.java
            ).build()
    }

    /**
     * Method to configure a CORS filter.
     *
     * @return CorsFilter - A CorsFilter instance configured with allowed origins, methods, and headers.
     */
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        config.allowedOrigins = allowedOrigins.split(",")
        config.allowedMethods = allowedMethods.split(",")
        config.allowedHeaders = allowedHeaders.split(",")

        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}