package com.ashone.cognitostarter.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.*

/**
 * Sample entity class for Person
 */
@Entity
@Table(name = "people") // Customize table name
class Person(
    @Id
    var id: UUID,

    @Column(nullable = false)
    var firstName: String,

    @Column
    var lastName: String,

    @Column
    var age: Int,

    @Column(unique = true, nullable = false)
    @get:NotBlank
    var phoneNumber: String,

    @Column
    var tag: String,

    @Column
    var createTime: Instant = Instant.now()

)
