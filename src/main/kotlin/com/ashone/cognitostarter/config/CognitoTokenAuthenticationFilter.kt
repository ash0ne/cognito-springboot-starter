package com.ashone.cognitostarter.config

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.filter.OncePerRequestFilter
import java.net.URI

/**
 * The Auth Filter class that has the logic to validate the Cognito JWT and extract the user info.
 */
class CognitoTokenAuthenticationFilter(
    private val jwksUrl: String,
    private val issuer: String,
    private val provider: String,
    private val userNameAttribute: String,
    private val rolesAttribute: String
) : OncePerRequestFilter() {
    /**
     * This method validates and extracts the user information from the Cognito token in the Auth header of the request.
     * If the Authorization header is present and contains a valid bearer token, the method extracts user information
     * and creates the authentication context to mark the user as authenticated.
     * If the Authorization header is missing or invalid, it returns an unauthorized response.
     *
     * @param request The HttpServletRequest object representing the HTTP request.
     * @param response The HttpServletResponse object representing the HTTP response.
     * @param filterChain The FilterChain object for invoking the next filter in the chain.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val decodedJWT: DecodedJWT
        val authorizationHeader = request.getHeader("Authorization")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7) // Remove "Bearer " prefix

            try {
                decodedJWT = verifyToken(token, jwksUrl)
            } catch (e: Exception) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Authorization header")
                return
            }

            // Extract necessary user information from the token
            val username = decodedJWT.claims[userNameAttribute]?.asString()
            logger.info("Got a valid bearer for user: $username")
            val roles = decodedJWT.claims[rolesAttribute]?.asList(String::class.java)
            logger.info("User $username has the roles: $roles")

            if (username != null) {
                // Create OAuth2AuthenticationToken with the username as the principal
                val authentication = OAuth2AuthenticationToken(
                    DefaultOAuth2User(roles?.map { role ->
                        SimpleGrantedAuthority("ROLE_$role")
                    } ?: emptyList(),
                        mapOf(Pair(userNameAttribute, username)), userNameAttribute),
                    roles?.map { role ->
                        SimpleGrantedAuthority("ROLE_$role")
                    } ?: emptyList(),
                    provider // OAuth2 provider name
                )

                logger.info("Setting auth context.")
                // Set the authentication context and mark user authenticated.
                SecurityContextHolder.getContext().authentication = authentication
                SecurityContextHolder.getContext().authentication.isAuthenticated = true
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unable to get username from token")
                return
            }
        } else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Missing or invalid Authorization header")
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun verifyToken(token: String, jwksUrl: String): DecodedJWT {
        val jwkProvider = JwkProviderBuilder(URI(jwksUrl).toURL()).cached(
            10, 24, java.util.concurrent.TimeUnit.HOURS // caching for 10 hours
        ).rateLimited(10, 1, java.util.concurrent.TimeUnit.MINUTES) // rate limiting
            .build()

        val jwt = JWT.decode(token)
        val jwk = jwkProvider.get(jwt.keyId)
        val algorithm = Algorithm.RSA256(jwk.publicKey as java.security.interfaces.RSAPublicKey, null)
        val verifier = JWT.require(algorithm).withIssuer(issuer).build()

        return verifier.verify(token)
    }
}