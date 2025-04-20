package dev.alireza.fms.dashboard.admin

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.common.exception.ResponseException
import dev.alireza.fms.jooq.tables.records.MarketsRecord
import dev.alireza.fms.market.MarketRepository
import dev.alireza.fms.market.MarketType
import dev.alireza.fms.openapi.api.AdminMarketApi
import dev.alireza.fms.openapi.model.MarketDTO
import dev.alireza.fms.openapi.model.MarketRequestBody
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class AdminMarketController(
    private val marketRepository: MarketRepository
) : AdminMarketApi {

    override fun createMarket(marketRequestBody: MarketRequestBody): ResponseApi<MutableList<MarketDTO>> {
        if (!marketRepository.isExistByName(marketRequestBody.name)) {
            val marketType = MarketType.fromString(marketRequestBody.type.value)
                ?: throw ResponseException(
                    HttpStatus.BAD_REQUEST,
                    ApiErrorConstant.INVALID_MARKET_TYPE
                )
            marketRepository.insertMarket(
                name = marketRequestBody.name,
                marketType = marketType,
                description = marketRequestBody.description
            )
            return ResponseApi(
                data = marketRepository.findAll().map { it.toDto() }.toMutableList(),
                message = "success"
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.MARKET_EXISTS
        )
    }

    override fun deleteMarket(marketId: Int): ResponseApi<MutableList<MarketDTO>> {
        if (marketRepository.isExistById(marketId)) {
            marketRepository.deleteById(marketId)
            return ResponseApi(
                data = marketRepository.findAll().map { it.toDto() }.toMutableList(),
                message = "success"
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.MARKET_NOT_FOUND
        )
    }

    fun MarketsRecord.toDto(): MarketDTO {
        return MarketDTO()
            .id(this.id)
            .name(this.name)
            .type(this.marketType)
            .description(this.description)
    }
}