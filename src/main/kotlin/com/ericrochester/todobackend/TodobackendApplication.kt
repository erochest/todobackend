package com.ericrochester.todobackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodobackendApplication

fun main(args: Array<String>) {
	runApplication<TodobackendApplication>(*args)
}
