package com.ericrochester.todobackend

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
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

    @Test
    fun whenFindAllWithEmptyDatabase_thenReturnsEmptyList() {
        val todoItems = todoRepository.findAll()
        todoItems.shouldBeEmpty()
    }

    @Test
    fun whenSavedAnItem_thenItIsReturned() {
        todoRepository.save(TodoItem(-1, "a title"))
        todoRepository.save(TodoItem(-1, "another title"))

        val todoItemList = todoRepository.findAllByTitle("a title")
        todoItemList.size shouldBeEqualTo 1
        todoItemList[0] shouldBeEqualTo TodoItem(1, "a title")

        val anotherList = todoRepository.findAllByTitle("another title")
        anotherList.size shouldBeEqualTo 1
        anotherList[0] shouldBeEqualTo TodoItem(2, "another title")
    }
}