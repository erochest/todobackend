package com.ericrochester.todobackend

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = arrayOf(TodobackendController::class))
class TodobackendControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    val objectMapper = ObjectMapper()

    // So I created this interface. I changed all of the tests below to use this
    // to set up the data that the controller will return.
    @MockkBean
    private lateinit var todoRepository: TodoRepository

    @Test
    fun whenRootGet_thenReturn200() {
        // For example
        every { todoRepository.findAll() } returns emptyList()
        getTodoList()
            .andExpect(status().is2xxSuccessful)
    }

    @Test
    fun whenRootPost_thenReturnTitle() {
        every { todoRepository.save(TodoItem(title="something")) }
            .returns(TodoItem(1, "something"))

        postTodo("something")
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title", equalTo("something")))
    }

    // I'd wondered if Mockk would just default to returning `void`.
    @Test
    fun whenRootDelete_thenReturnsOk() {
        // The spec seems underspecified here. Imma going to just blow everything away.
        justRun { todoRepository.deleteAll() }
        every { todoRepository.findAll() } returns emptyList()
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun whenPostThenGet_thenReturnsNewItems() {
        // And this came up. Basically, this should be tested in the repository, not
        // here. Maybe I'll replace this with a test that makes sure GET `/api` returns
        // whatever is in the repository.
        //
        // I'm going to keep this test here for the time being, but really it is testing
        // the repository, not the controller. So I'll eventually take this away.
        every { todoRepository.save(TodoItem(title="this is a title")) }
            .returns(TodoItem(1, "this is a title"))
        every { todoRepository.findAll() } returns listOf(TodoItem(1, "this is a title"))
        postTodo("this is a title")
        getTodoList()
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].title", equalTo("this is a title")))
    }

    @Test
    fun whenPostThenGet_thenNewItemsIsNotComplete() {
        every { todoRepository.save(TodoItem(title="placeholder")) }
            .returns(TodoItem(1, "placeholder"))
        postTodo("placeholder")
            .andDo { result ->
                println(result.response.contentAsString)
            }
            .andExpect(jsonPath("$", hasKey("completed")))
            .andExpect(jsonPath("$.completed", `is`(false)))
    }

    @Test
    fun whenPostThenGet_thenNewItemsHaveUrlString() {
        every { todoRepository.save(TodoItem(title = "has link")) }
            .returns(TodoItem(1, "has link"))
        every { todoRepository.findAll() }
            .returns(listOf(TodoItem(1, "has link")))
        postTodo("has link")
        getTodoList()
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].url", isA<Any>(String::class.java)))
    }

    // Once again, it's done too much. But whatev.

    @Disabled
    @Test
    fun whenPostThenGet_thenNewItemLinkReturnsTodo() {
        postTodo("has link resource")
        val result = getTodoList().andReturn().response.contentAsString
        val todo: TodobackendController.ItemResponse = objectMapper.readValue(
            result,
            TodobackendController.ItemResponse::class.java
            )
        mockMvc.perform(
            MockMvcRequestBuilders.get(todo.title) // url)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title", equalTo("has link resource")))
    }

    private fun getTodoList() = mockMvc.perform(
        MockMvcRequestBuilders.get("/api")
            .contentType(MediaType.APPLICATION_JSON)
    )

    private fun postTodo(title: String): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":  \"${title}\"}")
        )
    }
}
