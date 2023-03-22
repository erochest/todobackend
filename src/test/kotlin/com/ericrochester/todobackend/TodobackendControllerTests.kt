package com.ericrochester.todobackend

import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class TodobackendControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun testRootReturns200() {
        val size = hasSize<Any>(0)
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
    }
}