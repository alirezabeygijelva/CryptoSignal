package dev.alireza.fms.common.exception

import org.springframework.http.HttpStatusCode
import org.springframework.web.ErrorResponseException

class ResponseException(
    status: HttpStatusCode,
    val errorKind: ApiErrorKind
) : ErrorResponseException(status, Throwable(errorKind.message))
