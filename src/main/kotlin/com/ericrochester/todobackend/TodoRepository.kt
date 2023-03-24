package com.ericrochester.todobackend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

// No Flyway migrations! Tests driving out implementation. :D

interface TodoRepository : JpaRepository<TodoItem, Long> {
}

@Entity
data class TodoItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    val title: String = ""
) {
    constructor() : this(0, "") // required by JPA
}
