package com.ericrochester.todobackend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class TodobackendController {
    private val items = mutableListOf<String>()

    @GetMapping("/api")
    fun getApi(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(items)
    }

    @PostMapping("/api")
    fun postApi(@RequestBody request: ItemRequest): ResponseEntity<ItemResponse> {
        items.add(request.title)
        val response = ItemResponse(request.title)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    data class ItemRequest(val title: String)
    data class ItemResponse(val title: String)
}
