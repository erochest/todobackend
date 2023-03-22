package com.ericrochester.todobackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class TodobackendApplication {
	@Bean
	public fun corsConfigurer(): WebMvcConfigurer {
		return CorsConfiguration()
	}
}

fun main(args: Array<String>) {
	runApplication<TodobackendApplication>(*args)
}
