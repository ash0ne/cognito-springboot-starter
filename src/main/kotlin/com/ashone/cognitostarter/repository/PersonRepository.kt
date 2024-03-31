package com.ashone.cognitostarter.repository

import com.ashone.cognitostarter.repository.entity.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * JPA Repo interface for the Person entity.
 */
@Repository
interface PersonRepository : JpaRepository<Person, UUID>
