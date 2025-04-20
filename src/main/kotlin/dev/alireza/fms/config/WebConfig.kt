package dev.alireza.fms.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.function.RequestPredicates
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    @Value("\${spring.web.resources.static-locations}")
    private lateinit var resourceLocations: String

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("file:$resourceLocations")
    }

    @Bean
    fun spaRouter(): RouterFunction<ServerResponse> {
        val index = FileSystemResource(resourceLocations + "index.html")
        val extensions: List<String> =
            mutableListOf("js", "css", "ico", "png", "jpg", "gif", "eot", "woff", "woff2", "ttf", "json", "webp", "svg")
        val spaPredicate = RequestPredicates.path("/api/**")
            .or(RequestPredicates.path("/v3/api-docs/**"))
            .or(RequestPredicates.path("/swagger-ui/**"))
            .or(RequestPredicates.path("/swagger-ui.html"))
            .or(RequestPredicates.path("/webjars/swagger-ui/**"))
            .or(RequestPredicates.path("/images/**"))
            .or(RequestPredicates.path("/error"))
            .or(RequestPredicates.path("/ws/**"))
            .or(RequestPredicates.pathExtension { extention : String? -> extensions.contains(extention) }).negate()
        return RouterFunctions.route().resource(spaPredicate, index).build()
    }
}