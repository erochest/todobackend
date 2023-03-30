package com.ericrochester.todobackend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

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
        val todoItem = TodoItem(title = request.title, sortOrder = request.order)
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

    @Transactional
    @PatchMapping("/api/{id}")
    fun patchApiById(@PathVariable id: Long, @RequestBody request: UpdateItemRequest): ResponseEntity<ItemResponse> {
        val optionalItem = todoRepository.findById(id)
        if (optionalItem.isPresent) {
            if (request.title != null) {
                todoRepository.updateTitle(id, request.title)
            }
            if (request.completed != null) {
                todoRepository.updateCompleted(id, request.completed)
            }
            if (request.order != null) {
                todoRepository.updateSortOrder(id, request.order)
            }
            val newItem = todoRepository.findById(id).get()
            return ResponseEntity.ok(newItem.toItemResponse())
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    @Transactional
    @DeleteMapping("/api/{id}")
    fun deleteApiById(@PathVariable id: Long): ResponseEntity<Unit> {
        val optionalItem = todoRepository.findById(id)
        if (optionalItem.isPresent) {
            todoRepository.deleteById(id)
            return ResponseEntity.ok().build()
        } else {
            return ResponseEntity.notFound().build()
        }
    }

    data class ItemRequest(val title: String, val order: Long = -1)

    data class UpdateItemRequest(val title: String?, val completed: Boolean?, val order: Long?)

    data class ItemResponse(val id: Long, val completed: Boolean, val title: String, val url: String, val order: Long = -1) {
        companion object {
            fun fromTodoItem(item: TodoItem): ItemResponse {
                val url = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/api/{id}")
                    .buildAndExpand(item.id)
                    .toUri()
                    .toString()
                return ItemResponse(item.id, item.completed, item.title, url, item.sortOrder)
            }
        }
    }

    private fun TodoItem.toItemResponse(): ItemResponse {
        return ItemResponse.fromTodoItem(this)
    }
}