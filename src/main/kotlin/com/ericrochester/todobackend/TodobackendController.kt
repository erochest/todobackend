package com.ericrochester.todobackend

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TodobackendController {
    @GetMapping("/api")
    fun getApi(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, world!")
    }
}