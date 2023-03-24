package com.ericrochester.todobackend

import org.amshove.kluent.shouldBeEmpty
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

// Except, of course, it won't run because we haven't defined an implementation for
// the `TodoRepository`.
// I'm going to implement that now, but I don't think I've used the `SpringJPATest`
// before, so I'll lean on the robot for that.

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = arrayOf(
    "spring.jpa.hibernate.ddl-auto=validate"
))
class TodoRepositoryTests {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    // The assertion isn't needed yet. The test is broken as is.

    // :facepalm:
    @Test
    fun whenFindAllWithEmptyDatabase_returnsEmptyList() {
        val todoItems = todoRepository.findAll()
    }
}