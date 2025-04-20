package dev.alireza.fms.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Page
import org.springframework.http.ProblemDetail

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseApi<T>(
    @JsonProperty("data")
    val data: T? = null,
    @JsonProperty("pagination")
    val pagination: Pagination? = null,
    @JsonProperty("message")
    val message: String? = null,
    @JsonProperty("error")
    val error: ProblemDetail? = null
) {

    constructor(data: T?, page: Page<*>, message: String? = null, error: ProblemDetail? = null) : this(
        data = data,
        pagination = Pagination(
            page = page.number,
            size = page.size,
            totalPages = page.totalPages,
            total = page.totalElements.toInt()
        ),
        message = message,
        error = error
    )

    data class Pagination(
        @JsonProperty("page")
        val page: Int,
        @JsonProperty("size")
        val size: Int,
        @JsonProperty("totalPages")
        val totalPages: Int,
        @JsonProperty("total")
        val total: Int
    )
}
