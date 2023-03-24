package com.ericrochester.todobackend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
class TodobackendController(private val todoRepository: TodoRepository) {
    private val logger: Logger = LoggerFactory.getLogger(TodobackendController::class.java)

    @GetMapping("/api")
    fun getApi(): ResponseEntity<List<ItemResponse>> {
        val items = todoRepository.findAll()
        val response = items.map { ItemResponse(it.title, it.completed, generateUrlForItem(it.id)) }
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api")
    fun postApi(@RequestBody request: ItemRequest, uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<ItemResponse> {
        val todoItem = TodoItem(title = request.title)
        val newItem = todoRepository.save(todoItem)
        val url = uriComponentsBuilder.path("/api/{id}").buildAndExpand(newItem.id).toUriString()
        val response = ItemResponse(newItem.title, newItem.completed, url)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/api")
    fun deleteApi(): ResponseEntity<Unit> {
        todoRepository.deleteAll()
        return ResponseEntity.ok().build()
    }

    data class ItemRequest(val title: String)
    data class ItemResponse(val title: String, val completed: Boolean, val url: String)

    private fun generateUrlForItem(id: Long): String {
        return "/api/$id"
    }
}
