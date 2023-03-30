package com.ericrochester.todobackend

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockkStatic
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

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

    // I think this is good. Let's see.
    @Test
    fun whenPostThenGet_thenNewItemLinkReturnsTodo() {
        every { todoRepository.save(TodoItem(title = "has link resource")) }
            .returns(TodoItem(1, "has link resource"))
        every { todoRepository.findById(1) }
            .returns(Optional.of(TodoItem(1, "has link resource")))

        val result = postTodo("has link resource").andReturn().response.contentAsString
        val todo: TodobackendController.ItemResponse = objectMapper.readValue(
            result,
            TodobackendController.ItemResponse::class.java
            )

        mockMvc.perform(
            MockMvcRequestBuilders.get(todo.url)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title", equalTo("has link resource")))
    }

    @Test
    fun whenPatchTitle_thenUpdatesItemsTitle() {
        justRun { todoRepository.updateTitle(1, "new title") }
        every { todoRepository.findById(1) }
            .returns(Optional.of(TodoItem(1, "old title")))
            .andThenAnswer { Optional.of(TodoItem(1, "new title")) }

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"new title\"}")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title", equalTo("new title")))

        verify { todoRepository.updateTitle(1, "new title") }
    }

    @Test
    fun whenPatchCompleted_thenUpdatesItemsCompleted() {
        justRun { todoRepository.updateCompleted(1, true) }
        every { todoRepository.findById(1) }
            .returns(Optional.of(TodoItem(1, "old title")))
            .andThenAnswer { Optional.of(TodoItem(1, "old title", true)) }

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"completed\": true}")
        )
            .andDo { result ->
                println(result.response.contentAsString)
            }
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.completed", equalTo(true)))

        verify { todoRepository.updateCompleted(1, true) }
    }

    @Test
    fun whenGetUrl_thenReturnsSimplePath() {
        // I'm reaching too deeply in, but let's see what happens.
        val httpServletRequest = MockHttpServletRequest().apply {
            requestURI = "/api/3"
            method = "GET"
        }
        mockkStatic(RequestContextHolder::class)
        every { RequestContextHolder.getRequestAttributes() }
            .returns(ServletRequestAttributes(httpServletRequest))

        val todoItem = TodoItem(3, "title")
        val itemResponse = TodobackendController.ItemResponse.fromTodoItem(todoItem)
        itemResponse.url.shouldBeEqualTo("http://localhost/api/3")
    }

    @Test
    fun whenDeleteToTodoItemUrl_thenDeletesItem() {
        justRun { todoRepository.deleteById(1) }
        every { todoRepository.findById(1) }
            .returns(Optional.of(TodoItem(1, "old title")))

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)

        verify { todoRepository.deleteById(1) }
    }

    @Test
    fun whenPostWithOrder_thenResponseHasOrderField() {
        every { todoRepository.save(TodoItem(title = "has order", sortOrder = 42)) }
            .returns(TodoItem(1, "has order", sortOrder = 42))
        every { todoRepository.findAll() }
            .returns(listOf(TodoItem(1, "has order", sortOrder = 42)))

        postTodo("has order", order = 42)

        getTodoList()
            .andExpect(jsonPath("$", hasSize<Any>(1)))
            .andExpect(jsonPath("$[0].order", equalTo(42)))
    }

    @Test
    fun whenPatchWithOrder_thenTodoItemOrderChanges() {
        justRun { todoRepository.updateSortOrder(1, 42) }
        every { todoRepository.findById(1) }
            .returns(Optional.of(TodoItem(1, "old title", sortOrder = 99)))
            .andThenAnswer { Optional.of(TodoItem(1, "old title", sortOrder = 42)) }

        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"order\": 42}")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.order", equalTo(42)))

        verify { todoRepository.updateSortOrder(1, 42) }
    }

    private fun getTodoList() = mockMvc.perform(
        MockMvcRequestBuilders.get("/api")
            .contentType(MediaType.APPLICATION_JSON)
    )

    private fun postTodo(title: String, order: Long = -1): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":  \"${title}\", \"order\": ${order}}")
        )
    }
}
