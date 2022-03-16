package net.liccioni.gateway

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
class GatewayApplication {

    @Value("\${FRONTEND_HOST:frontend}")
    private lateinit var frontendHostName: String

    @Value("\${FRONTEND_PORT:3000}")
    private lateinit var frontendPort: String

    @Value("\${ACCOUNT_SERVICE:http://localhost:8082}")
    private lateinit var accountServiceUrl: String

    @Bean
    fun myRoutes(builder: RouteLocatorBuilder): RouteLocator = builder.routes().route { p ->
        p.path("/api/persons/**", "/api/profile/persons/**")
            .filters { f ->
                f.setHostHeader(frontendHostName).addRequestHeader("x-forwarded-port", frontendPort)
            }
            .uri(accountServiceUrl)
    }.build()
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
