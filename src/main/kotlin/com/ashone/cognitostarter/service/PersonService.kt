package com.ashone.cognitostarter.service

import com.ashone.cognitostarter.dto.PersonDTO
import com.ashone.cognitostarter.exception.ConflictException
import com.ashone.cognitostarter.repository.PersonRepository
import com.ashone.cognitostarter.repository.entity.Person
import com.ashone.cognitostarter.repository.specification.PersonSpecification
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service class to perform operations related to Person entity.
 */
@Service
class PersonService(private val personRepository: PersonRepository) {

    /**
     * Creates a new person.
     *
     * @param personDTO The data of the person to be created.
     * @return The created person.
     * @throws ConflictException If a person with the same UUID already exists.
     */
    fun create(personDTO: PersonDTO): Person {
        try {
            return personRepository.save(
                Person(
                    UUID.randomUUID(), personDTO.firstName, personDTO.lastName,
                    personDTO.age, personDTO.phoneNumber, personDTO.tag
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw ConflictException("Error : Possibly duplicate data for phone_number")
        }
    }

    /**
     * Retrieves a paginated list of all persons and searches by firstName and/or mobile if provided.
     *
     * @param page The page number (default: 0).
     * @param size The page size (default: 20).
     * @return A paginated list of persons.
     */
    fun findAllPaginated(name: String?, phone: String?, page: Int?, size: Int?): Page<Person> {
        val pageable = PageRequest.of(
            (page ?: 0).coerceAtLeast(0),
            (size ?: 20).coerceAtLeast(1),
            Sort.by(Sort.Direction.DESC, "createTime")
        )
        val specification = PersonSpecification.hasNameOrPhone(name, phone)
        return personRepository.findAll(specification, pageable)
    }

    /**
     * Retrieves a person by its UUID.
     *
     * @param id The UUID of the person.
     * @return The person if found, else null.
     */
    fun findById(id: UUID): Person? {
        return personRepository.findById(id).orElse(null)
    }

    /**
     * Updates an existing person.
     *
     * @param id The UUID of the person to be updated.
     * @param personDTO The updated data of the person.
     * @return The updated person if found, else null.
     */
    fun update(id: UUID, personDTO: PersonDTO): Person? {
        try {
            val personToUpdate = personRepository.getReferenceById(id)
            personToUpdate.firstName = personDTO.firstName
            personToUpdate.lastName = personDTO.lastName
            personToUpdate.age = personDTO.age
            personToUpdate.phoneNumber = personDTO.phoneNumber
            personToUpdate.tag = personDTO.tag

            return personRepository.save(personToUpdate)
        } catch (e: JpaObjectRetrievalFailureException) {
            return null
        } catch (e: DataIntegrityViolationException) {
            throw ConflictException("Error : Possibly duplicate data for phone_number")
        }
    }

    /**
     * Deletes a person by its UUID.
     *
     * @param id The UUID of the person to be deleted.
     */
    fun delete(id: UUID) {
        personRepository.deleteById(id)
    }
}
