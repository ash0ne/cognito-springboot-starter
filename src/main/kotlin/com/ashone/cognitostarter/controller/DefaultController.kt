package com.ashone.cognitostarter.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

/**
 * The Default Controller class.
 *
 * This class provides the util/test endpoints that validate various functions of the app.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(
    name = "Default Controller",
    description = "A collection of util and test endpoints that validate various functions of the app."
)
class DefaultController {

    /**
     * Handles GET requests to "/hello" endpoint.
     *
     * @return Any - Returns a response containing the authenticated user, their roles and the current time.
     */
    @GetMapping("/hello")
    fun hello(): Any {
        data class HelloResponse(val message: String, val roles: List<String>)

        val username = SecurityContextHolder.getContext().authentication.name
        val roles = SecurityContextHolder.getContext().authentication.authorities.map { grantedAuthority ->
            grantedAuthority.authority
        }.toList()
        return HelloResponse("Hello $username at ${LocalDateTime.now()}", roles)
    }
}
