package com.ericrochester.todobackend

import com.fasterxml.jackson.databind.ObjectMapper
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

    @Test
    fun whenRootGet_thenReturn200() {
        getTodoList()
            .andExpect(status().is2xxSuccessful)
    }

    @Test
    fun whenRootPost_thenReturnTitle() {
        postTodo("something")
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title", equalTo("something")))
    }

    @Test
    fun whenRootDelete_thenReturnsOk() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
        )
            .andExpect(status().isOk)
    }

    @Disabled
    @Test
    fun whenPostThenGet_thenReturnsNewItems() {
        postTodo("this is a title")
        getTodoList()
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].title", equalTo("this is a title")))
    }

    @Disabled
    @Test
    fun whenPostThenGet_thenNewItemsIsNotComplete() {
        postTodo("placeholder")
        getTodoList()
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].completed", `is`(false)))
    }

    @Disabled
    @Test
    fun whenPostThenGet_thenNewItemsHaveUrlString() {
        postTodo("has link")
        getTodoList()
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].url", isA<Any>(String::class.java)))
    }

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
