package com.ericrochester.todobackend

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfiguration : WebMvcConfigurer {

    private val logger = LoggerFactory.getLogger(CorsConfiguration::class.java)

    init {
        logger.info("Initializing CORS configuration...")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        logger.info("Configuring CORS headers")
        registry.addMapping("/**")
            .allowedOrigins("*")
    }
}
