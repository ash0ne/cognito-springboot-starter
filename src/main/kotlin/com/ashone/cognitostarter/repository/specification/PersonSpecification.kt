package com.ashone.cognitostarter.repository.specification

import com.ashone.cognitostarter.repository.entity.Person
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

/**
 * Static method to create the predicate and query filter for Person entity.
 *
 * return Specification
 */
object PersonSpecification {
    fun hasNameOrPhone(name: String?, phone: String?): Specification<Person?> {
        return Specification<Person?> { root: Root<Person?>, _: CriteriaQuery<*>?, criteriaBuilder: CriteriaBuilder ->
            val predicates: MutableList<Predicate> = ArrayList()
            if (!name.isNullOrEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%$name%"))
            }
            if (!phone.isNullOrEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%$phone%"))
            }
            criteriaBuilder.and(*predicates.map { it }.toTypedArray())
        }
    }
}
