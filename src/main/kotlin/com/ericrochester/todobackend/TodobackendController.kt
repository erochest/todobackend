package com.ericrochester.todobackend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

// It defaulted the repository property to `private`. Nice.
@RestController
class TodobackendController(private val todoRepository: TodoRepository) {

    @GetMapping("/api")
    fun getApi(): ResponseEntity<List<TodoItem>> {
        val items = todoRepository.findAll()
        return ResponseEntity.ok(items)
    }

    @PostMapping("/api")
    fun postApi(@RequestBody request: ItemRequest): ResponseEntity<ItemResponse> {
        val todoItem = TodoItem(title=request.title)
        todoRepository.save(todoItem)
        // Obviously the response object will need to change.
        return ResponseEntity.status(HttpStatus.CREATED).body(ItemResponse(todoItem.title))
    }

    @DeleteMapping("/api")
    fun deleteApi(): ResponseEntity<Unit> {
        todoRepository.deleteAll()
        return ResponseEntity.ok().build()
    }

    data class ItemRequest(val title: String)
    data class ItemResponse(val title: String)
}
