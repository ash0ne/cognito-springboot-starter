package com.ashone.cognitostarter.controller

import com.ashone.cognitostarter.dto.PersonDTO
import com.ashone.cognitostarter.repository.entity.Person
import com.ashone.cognitostarter.service.PersonService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Controller class for managing Person entities.
 *
 * This class handles HTTP requests related to Person operations.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(
    name = "Person Controller",
    description = "Endpoints that allow operations on the Person entity."
)
class PersonController
/**
 * Constructs a new PersonController with the provided PersonService.
 * @param personService The service responsible for handling Person-related operations.
 */
    (private val personService: PersonService) {

    /**
     * Creates a new Person entity.
     *
     * @param personDTO The data representing the new Person.
     * @return The created Person entity.
     */
    @PostMapping("/persons")
    fun createPerson(@Valid @RequestBody personDTO: PersonDTO): Person {
        return personService.create(personDTO)
    }

    /**
     * Retrieves all Person entities with pagination support and optionally,
     * can search by firstName(name) and phoneNumber(phone).
     *
     * @param page The page number (optional).
     * @param size The page size (optional).
     * @return A Page object containing Person entities.
     */
    @GetMapping("/persons")
    fun getAllPersons(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) @Min(0) page: Int?,
        @RequestParam(required = false) @Min(1) size: Int?
    ): Page<Person> {
        return personService.findAllPaginated(name, phone, page, size)
    }

    /**
     * Retrieves a specific Person entity by its ID.
     *
     * @param id The ID of the Person entity to retrieve.
     * @return The Person entity, if found; otherwise, null.
     */
    @GetMapping("/persons/{id}")
    fun getPersonById(@PathVariable id: UUID): Person? {
        return personService.findById(id)
    }

    /**
     * Updates an existing Person entity.
     *
     * @param id The ID of the Person entity to update.
     * @param personDTO The updated data for the Person entity.
     * @return The updated Person entity, if found; otherwise, null.
     */
    @PatchMapping("/persons/{id}")
    fun updatePerson(@PathVariable id: UUID, @Valid @RequestBody personDTO: PersonDTO): Person? {
        return personService.update(id, personDTO)
    }

    /**
     * Deletes a specific Person entity by its ID.
     *
     * @param id The ID of the Person entity to delete.
     */
    @DeleteMapping("/persons/{id}")
    fun deletePerson(@PathVariable id: UUID) {
        personService.delete(id)
    }
}
