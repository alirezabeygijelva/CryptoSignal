package dev.alireza.fms.dashboard.admin

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.common.exception.ResponseException
import dev.alireza.fms.market.binance.BinanceRestartEvent
import dev.alireza.fms.market.binance.BinanceStreamNameRepository
import dev.alireza.fms.openapi.api.BinanceApi
import dev.alireza.fms.openapi.model.BinanceStreamDTO
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class BinanceController(
    private val binanceStreamRepository: BinanceStreamNameRepository,
    private val eventPublisher: ApplicationEventPublisher
): BinanceApi {

    override fun createBinanceStream(binanceStreamDTO: BinanceStreamDTO): ResponseApi<MutableList<BinanceStreamDTO>> {
        if (!binanceStreamRepository.isExistByName(binanceStreamDTO.streamName)) {
            binanceStreamRepository.insertNewStream(binanceStreamDTO.streamName)
            eventPublisher.publishEvent(BinanceRestartEvent(0, this))
            return ResponseApi(
                data = binanceStreamRepository.getAllStreamNames()
                    .map { name -> BinanceStreamDTO().streamName(name) }.toMutableList(),
                message = "success"
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.BINANCE_STREAM_EXISTS
        )
    }

    override fun deleteBinanceStream(streamName: String): ResponseApi<MutableList<BinanceStreamDTO>> {
        if (binanceStreamRepository.isExistByName(streamName)) {
            binanceStreamRepository.deleteByName(streamName)
            eventPublisher.publishEvent(BinanceRestartEvent(0, this))
            return ResponseApi(
                data = binanceStreamRepository.getAllStreamNames()
                    .map { name -> BinanceStreamDTO().streamName(name) }.toMutableList(),
                message = "success"
            )
        }
        throw ResponseException(
            HttpStatus.NOT_FOUND,
            ApiErrorConstant.BINANCE_STREAM_NOT_FOUND
        )
    }

    override fun getBinanceStreams(): ResponseApi<MutableList<BinanceStreamDTO>> {
        return ResponseApi(
            data = binanceStreamRepository.getAllStreamNames()
                .map { name -> BinanceStreamDTO().streamName(name) }.toMutableList(),
            message = "success"
        )
    }
}