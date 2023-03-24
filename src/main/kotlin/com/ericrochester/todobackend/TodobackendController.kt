package com.ericrochester.todobackend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// It defaulted the repository property to `private`. Nice.
@RestController
class TodobackendController(private val todoRepository: TodoRepository) {
    private val logger: Logger = LoggerFactory.getLogger(TodobackendController::class.java)

    @GetMapping("/api")
    fun getApi(): ResponseEntity<List<ItemResponse>> {
        val items = todoRepository.findAll()
        return ResponseEntity.ok(items.map { ItemResponse(it.title, it.completed) })
    }

    @PostMapping("/api")
    fun postApi(@RequestBody request: ItemRequest): ResponseEntity<ItemResponse> {
        val todoItem = TodoItem(title=request.title)
        val newItem = todoRepository.save(todoItem)
        val response = ItemResponse(newItem.title, newItem.completed)
        // Obviously the response object will need to change.
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/api")
    fun deleteApi(): ResponseEntity<Unit> {
        todoRepository.deleteAll()
        return ResponseEntity.ok().build()
    }

    data class ItemRequest(val title: String)
    data class ItemResponse(val title: String, val completed: Boolean)
}
