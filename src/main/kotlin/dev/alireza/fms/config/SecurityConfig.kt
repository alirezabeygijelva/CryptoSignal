package dev.alireza.fms.config

import dev.alireza.fms.security.BearerAccessDeniedHandler
import dev.alireza.fms.security.BearerAuthenticationEntryPoint
import dev.alireza.fms.security.BearerAuthenticationFilter
import dev.alireza.fms.security.BearerAuthenticationProvider
import dev.alireza.fms.security.jwt.JwtUtils
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.security.authentication.AuthenticationEventPublisher
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig(
    private val authenticationEntryPoint: BearerAuthenticationEntryPoint,
    private val bearerAccessDeniedHandler: BearerAccessDeniedHandler,
    private val userDetailsService: UserDetailsService,
    private val jwtUtils: JwtUtils,
    private val properties: ConfigProperties
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/webjars/swagger-ui/**",
                        "/api/v*/public/**",
                        "/api/v*/auth/token",
                        "/api/v*/auth/signup",
                        "/api/v*/auth/request-reset-password",
                        "/api/v*/auth/reset-password",
                        "/actuator/health",
                        "/ws/**"
                    ).permitAll()
                    .requestMatchers("/api/v*/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationManager(authenticationManager())
            .addFilterBefore(
                BearerAuthenticationFilter(authenticationManager(), userDetailsService, jwtUtils),
                BasicAuthenticationFilter::class.java
            )
            /*.exceptionHandling { exception ->
                exception
                    .accessDeniedHandler(bearerAccessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint)
            }*/

        return http.build()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val manager = ProviderManager(
            BearerAuthenticationProvider(jwtUtils),
        )
        manager.setAuthenticationEventPublisher(DefaultAuthenticationEventPublisher())
        manager.isEraseCredentialsAfterAuthentication = true
        return manager
    }

    @Bean
    fun authenticationEventPublisher(applicationEventPublisher: ApplicationEventPublisher?): AuthenticationEventPublisher {
        return DefaultAuthenticationEventPublisher(applicationEventPublisher)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun simpleCorsFilter(): FilterRegistrationBean<CorsFilter> {
        val source = UrlBasedCorsConfigurationSource()
        val cors = CorsConfiguration()
        cors.allowCredentials = true

        // URL below needs to match the front-end client URL and port
        cors.allowedOrigins = properties.allowOrigins.toList()
        cors.setAllowedOriginPatterns(properties.allowOriginPatterns.toList())
        cors.allowedMethods = listOf(CorsConfiguration.ALL)
        cors.allowedHeaders = listOf(CorsConfiguration.ALL)

        source.registerCorsConfiguration("/**", cors)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE
        return bean
    }
}