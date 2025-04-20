package dev.alireza.fms.dashboard.general

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.common.exception.ResponseException
import dev.alireza.fms.jooq.tables.records.MarketsRecord
import dev.alireza.fms.market.Asset
import dev.alireza.fms.market.AssetRepositry
import dev.alireza.fms.market.MarketRepository
import dev.alireza.fms.openapi.api.PublicMarketApi
import dev.alireza.fms.openapi.model.AssetDTO
import dev.alireza.fms.openapi.model.MarketDTO
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/public")
class PublicMarketController(
    private val marketRepository: MarketRepository,
    private val assetRepositry: AssetRepositry
) : PublicMarketApi {

    override fun getAllAssets(marketId: Int, pageable: Pageable?): ResponseApi<MutableList<AssetDTO>> {
        val marketName = marketRepository.findById(marketId).orElseThrow {
            throw ResponseException(
                HttpStatus.NOT_FOUND,
                ApiErrorConstant.MARKET_NOT_FOUND
            )
        }.name
        return ResponseApi(
            data = assetRepositry
                .findAll()
                .filter { it != null && it.marketName == marketName }
                .map { it.toDto() }.toMutableList(),
            message = "success"
        )
    }

    override fun getAllMarkets(pageable: Pageable): ResponseApi<MutableList<MarketDTO>> {
        val page = marketRepository.findAll(pageable)
        return ResponseApi(
            data = page.map { it.toDto() }.toMutableList(),
            message = "success",
            page = page
        )
    }

    override fun getAsset(marketId: Int, symbol: String): ResponseApi<AssetDTO> {
        val marketName = marketRepository.findById(marketId).orElseThrow {
            throw ResponseException(
                HttpStatus.NOT_FOUND,
                ApiErrorConstant.MARKET_NOT_FOUND
            )
        }.name
        val asset = assetRepositry.findById(symbol).orElseThrow {
            throw ResponseException(
                HttpStatus.NOT_FOUND,
                ApiErrorConstant.ASSETS_NOT_FOUND
            )
        }
        if (asset.marketName != marketName) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.SYMBOL_NOT_MATCH
            )
        }
        return ResponseApi(
            data = asset.toDto(),
            message = "success"
        )
    }

    override fun getMarket(marketId: Int): ResponseApi<MarketDTO> {
        val market = marketRepository.findById(marketId).orElseThrow {
            throw ResponseException(
                HttpStatus.NOT_FOUND,
                ApiErrorConstant.MARKET_NOT_FOUND
            )
        }
        return ResponseApi(
            data = market.toDto(),
            message = "success"
        )
    }

    fun Asset.toDto(): AssetDTO {
        return AssetDTO()
            .symbol(this.symbol)
            .marketName(this.marketName)
            .openPrice(this.openPrice)
            .closePrice(this.closePrice)
            .highPrice(this.highPrice)
            .lowPrice(this.lowPrice)
            .volume(this.volume)
            .openTime(this.openTime)
            .closeTime(this.closeTime)
            .updatedAt(this.updatedAt)
    }

    fun MarketsRecord.toDto(): MarketDTO {
        return MarketDTO()
            .id(this.id)
            .name(this.name)
            .type(this.marketType)
            .description(this.description)
    }
}