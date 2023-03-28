package com.ericrochester.todobackend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

// No Flyway migrations! Tests driving out implementation. :D

interface TodoRepository : JpaRepository<TodoItem, Long> {
    abstract fun findAllByTitle(title: String): List<TodoItem>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update TodoItem set title = ?2 where id = ?1")
    abstract fun updateTitle(id: Long, title: String)
}

@Entity
data class TodoItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    var title: String = "",
    val completed: Boolean = false
) {
    constructor() : this(0, "", false) // required by JPA
}
