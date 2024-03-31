package com.ashone.cognitostarter.exception.handler

import com.ashone.cognitostarter.exception.ConflictException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

/**
 * Global exception handler class for handling functional exceptions and constructing the error response.
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * Handler for the ConflictException RuntimeException.
     *
     * @param ex the ConflictException.
     * @param request The HTTP request object.
     * @return ResponseEntity with the Error Response.
     */
    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(ex: ConflictException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            LocalDateTime.now(), HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.reasonPhrase, ex.message ?: "", request.requestURI
        )
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(response)
    }

    /**
     * Handler for the MethodArgumentNotValidException.
     *
     * @param ex the MethodArgumentNotValidException.
     * @param request The HTTP request object.
     * @return ResponseEntity with the ErrorResponse body having all the validation failures set as message.
     */
    @ExceptionHandler(MethodArgumentNotValidException::class, BindException::class)
    fun handleValidationExceptions(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val errors = mutableListOf<String>()
        if (ex is MethodArgumentNotValidException) {
            errors.addAll(ex.bindingResult.fieldErrors.map { fieldError ->
                " ${fieldError.defaultMessage}"
            })
        } else if (ex is BindException) {
            errors.addAll(ex.bindingResult.fieldErrors.map { fieldError ->
                "${fieldError.defaultMessage}"
            })
        }
        val response = ErrorResponse(
            LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.reasonPhrase,
            "{$errors}", request.requestURI
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }
}
