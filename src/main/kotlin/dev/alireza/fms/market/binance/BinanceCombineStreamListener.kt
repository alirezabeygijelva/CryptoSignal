package dev.alireza.fms.market.binance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import dev.alireza.fms.config.ConfigProperties
import dev.alireza.fms.market.Asset
import dev.alireza.fms.market.MarketAssetService
import okhttp3.Response
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BinanceCombineStreamListener(
    private val marketAssetService: MarketAssetService,
    private val objectMapper: ObjectMapper,
    private val eventPublisher: ApplicationEventPublisher,
    private val properties: ConfigProperties,
    private val simpMessagingTemplate: SimpMessagingTemplate
) : BinanceStreamListener {

    private val marketName: String = "Binance"
    private val logger: Logger = LoggerFactory.getLogger(BinanceCombineStreamListener::class.java)

    override fun onOpen(response: Response?) {
        logger.info("[Binance Combine Stream Listener] - The connection was opened. response: {}", response)
    }

    override fun onMessage(data: String?) {
        if (data == null) return
        try {
            val message = objectMapper.readValue(data, WebSocketMessage::class.java)
            val streamType = StreamType.findContains(message.stream)
            when (streamType) {
                StreamType.AGG_TRADE -> return
                StreamType.TRADE -> return
                StreamType.KLINE -> processKlineData(message.data)
                StreamType.MINI_TICKER -> return
                StreamType.TICKER -> processTickerData(message.data)
                StreamType.BOOK_TIKER -> return
                StreamType.AVG_PRICE -> return
                StreamType.UNKNOWN -> return
            }
        } catch (e: Exception) {
            logger.error("[Binance Combine Stream Listener] - It is not possible to process the data.", e)
        }
    }

    override fun onClosing(code: Int, reason: String?) {
        logger.warn("[Binance Combine Stream Listener] - The connection is closing. code: {}, reason: {}", code, reason)
    }

    override fun onClosed(code: Int, reason: String?) {
        logger.warn("[Binance Combine Stream Listener] - The connection was closed. code: {}, reason: {}", code, reason)
        // TODO: Implement a mechanism to reconnect.
    }

    override fun onFailure(t: Throwable?, response: Response?) {
        logger.error("[Binance Combine Stream Listener] - The connection encountered an error.")
        eventPublisher.publishEvent(BinanceRestartEvent(30_000, this))
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class WebSocketMessage(
        @JsonProperty("stream") val stream: String,
        @JsonProperty("data") val data: Map<String, Any>
    )

    enum class StreamType(val value: String) {
        AGG_TRADE("aggTrade"),
        TRADE("trade"),
        KLINE("kline"),
        MINI_TICKER("miniTicker"),
        TICKER("ticker"),
        BOOK_TIKER("bookTicker"),
        AVG_PRICE("avgPrice"),
        UNKNOWN("");

        companion object {
            fun fromValue(value: String): StreamType {
                return entries.find { it.value == value } ?: UNKNOWN
            }

            fun findContains(value: String): StreamType {
                return entries.find { value.contains(it.value) } ?: UNKNOWN
            }
        }
    }

    // -------------------------------
    // Private Helper
    // -------------------------------

    private fun processKlineData(data: Map<String, Any>) {
        val kline = data["k"] as Map<*, *>
        val asset = Asset(
            data["s"] as String,
            marketName,
            kline["o"].toString().toDouble(),
            kline["c"].toString().toDouble(),
            kline["h"].toString().toDouble(),
            kline["l"].toString().toDouble(),
            kline["v"].toString().toDouble(),
            kline["t"].toString().toLong(),
            kline["T"].toString().toLong(),
            Instant.now().toEpochMilli(),
            properties.market.general.cacheTtl
        )
        marketAssetService.save(asset)
        simpMessagingTemplate.convertAndSend("/topic/binance/kline", data)
    }

    private fun processTickerData(data: Map<String, Any>) {
        simpMessagingTemplate.convertAndSend("/topic/binance/ticker", data)
    }
}