package com.ericrochester.todobackend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class TodobackendController {
    private val items = mutableListOf<ItemRequest>()

    @GetMapping("/api")
    fun getApi(): ResponseEntity<List<ItemRequest>> {
        return ResponseEntity.ok(items)
    }

    @PostMapping("/api")
    fun postApi(@RequestBody request: ItemRequest): ResponseEntity<ItemResponse> {
        items.add(request)
        val response = ItemResponse(request.title)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @DeleteMapping("/api")
    fun deleteApi(): ResponseEntity<Unit> {
        items.clear()
        return ResponseEntity.ok().build()
    }

    data class ItemRequest(val title: String)
    data class ItemResponse(val title: String)
}
