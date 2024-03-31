package com.ashone.cognitostarter.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

/**
 * DTO representing input/output for a Person object.
 **/
data class PersonDTO(
    @field:NotEmpty(message = "First name must not be blank")
    val firstName: String,
    val lastName: String,
    val age: Int,
    @field:NotBlank(message = "Phone Number name must not be blank")
    val phoneNumber: String,
    val tag: String
)
