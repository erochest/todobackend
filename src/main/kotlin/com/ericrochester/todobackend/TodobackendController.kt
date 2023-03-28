package com.ericrochester.todobackend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder

@RestController
class TodobackendController(private val todoRepository: TodoRepository) {
    private val logger: Logger = LoggerFactory.getLogger(TodobackendController::class.java)

    @GetMapping("/api")
    fun getApi(): ResponseEntity<List<ItemResponse>> {
        val items = todoRepository.findAll()
        val response = items.map { item -> item.toItemResponse() }
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api")
    fun postApi(@RequestBody request: ItemRequest): ResponseEntity<ItemResponse> {
        val todoItem = TodoItem(title = request.title)
        val newItem = todoRepository.save(todoItem)
        val response = newItem.toItemResponse()
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/api/{id}")
    fun getApiById(@PathVariable id: Long): ResponseEntity<ItemResponse> {
        val optionalItem = todoRepository.findById(id)
        if (optionalItem.isPresent) {
            val item = optionalItem.get()
            val response = item.toItemResponse()
            return ResponseEntity.ok(response)
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/api")
    fun deleteApi(): ResponseEntity<Unit> {
        todoRepository.deleteAll()
        return ResponseEntity.ok().build()
    }

    /// When PATCH is called, update the title
    @PatchMapping("/api/{id}")
    fun patchApiById(@PathVariable id: Long, @RequestBody request: ItemRequest): ResponseEntity<ItemResponse> {
        val optionalItem = todoRepository.findById(id)
        if (optionalItem.isPresent) {
            todoRepository.updateTitle(id, request.title)
            val newItem = todoRepository.findById(id).get()
            return ResponseEntity.ok(newItem.toItemResponse())
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    fun getUrl(id: Long): String {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
            .toString()
    }

    data class ItemRequest(val title: String)

    data class ItemResponse(val title: String, val completed: Boolean, val url: String) {
        companion object {
            fun fromTodoItem(item: TodoItem, url: String): ItemResponse {
                return ItemResponse(item.title, item.completed, url)
            }
        }
    }

    private fun TodoItem.toItemResponse(): ItemResponse {
        return ItemResponse.fromTodoItem(this, getUrl(id))
    }
}