package dev.alireza.fms.common.advice

import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.common.exception.ResponseException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI
import javax.naming.AuthenticationException

@RestControllerAdvice
class DefaultResposeExceptionHandler {

    @ExceptionHandler(ResponseException::class)
    fun defaultErrorHandler(
        request: HttpServletRequest,
        ex: ResponseException
    ): ResponseEntity<ResponseApi<Any>> {
        val error: ProblemDetail = ex.body
        error.type = URI.create(request.requestURI)
        error.setProperty("message", ex.errorKind.message)
        error.setProperty("kind", ex.errorKind.kind)
        return ResponseEntity.status(ex.statusCode).body(ResponseApi(error = error))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(
        request: HttpServletRequest,
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ResponseApi<Any>> {
        val error: ProblemDetail = ex.body
        error.type = URI.create(request.requestURI)
        error.setProperty("message", ex.message)
        error.setProperty("kind", "INVALID_ARGUMENT")
        return ResponseEntity.status(ex.statusCode).body(ResponseApi(error = error))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun authenticationExceptionHandler(
        request: HttpServletRequest,
        ex: AuthenticationException
    ): ResponseEntity<ResponseApi<Any>> {
        val error = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message)
        error.type = URI.create(request.requestURI)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseApi(error = error))
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsExceptionHandler(
        request: HttpServletRequest,
        ex: BadCredentialsException
    ): ResponseEntity<ResponseApi<Any>> {
        val error = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message)
        error.type = URI.create(request.requestURI)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseApi(error = error))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedExceptionHandler(
        request: HttpServletRequest,
        ex: AccessDeniedException
    ): ResponseEntity<ResponseApi<Any>> {
        val error = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.message)
        error.type = URI.create(request.requestURI)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseApi(error = error))
    }
}