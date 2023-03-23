package com.ericrochester.todobackend

import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = arrayOf(TodobackendController::class))
class TodobackendControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun whenRootGet_thenReturn200() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful)
    }

    @Test
    fun testWhenPostReturnTitle() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":  \"something\"}")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title", equalTo("something")))
    }

    @Test
    fun testWhenDeleteReturnsFulfilled() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
        )
            .andExpect(status().isOk)
    }
}