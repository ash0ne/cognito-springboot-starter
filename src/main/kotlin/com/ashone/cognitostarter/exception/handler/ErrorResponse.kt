package com.ashone.cognitostarter.exception.handler

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

/**
 * DTO representing the Error Response for expected API errors.
 **/
data class ErrorResponse(
    @JsonProperty("timestamp") val timestamp: LocalDateTime,
    @JsonProperty("status") val status: Int,
    @JsonProperty("error") val error: String,
    @JsonProperty("message") val message: String,
    @JsonProperty("path") val path: String
)
