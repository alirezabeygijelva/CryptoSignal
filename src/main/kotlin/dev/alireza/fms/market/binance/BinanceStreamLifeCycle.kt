package dev.alireza.fms.market.binance

import com.binance.connector.client.WebSocketStreamClient
import com.binance.connector.client.impl.WebSocketStreamClientImpl
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

@Configuration
class BinanceStreamLifeCycle(
    private val binanceCombineStreamListener: BinanceCombineStreamListener,
    private val binanceStreamNameRepository: BinanceStreamNameRepository
) {

    private val wsStreamClient: WebSocketStreamClient = WebSocketStreamClientImpl()
    private var streamId : Int = 0

    @Async
    @EventListener(BinanceStartEvent::class)
    fun onStart() {
        this.startCombineStream(binanceStreamNameRepository.getAllStreamNames())
    }

    @Async
    @EventListener(BinanceRestartEvent::class)
    fun onRestart(event: BinanceRestartEvent) {
        Thread.sleep(event.timeout)
        this.wsStreamClient.closeConnection(this.streamId)
        this.startCombineStream(binanceStreamNameRepository.getAllStreamNames())
    }

    @Async
    @EventListener(BinanceStopEvent::class)
    fun onStop() {
        this.wsStreamClient.closeAllConnections()
    }

    // ---------------------------
    // Private Helper
    // ---------------------------

    private fun startCombineStream(symbols: List<String>) {
        this.streamId = this.wsStreamClient.combineStreams(
            symbols.toCollection(ArrayList()),
            { response -> binanceCombineStreamListener.onOpen(response) },
            { data -> binanceCombineStreamListener.onMessage(data) },
            { code, reason -> binanceCombineStreamListener.onClosing(code, reason) },
            { code, reason -> binanceCombineStreamListener.onClosed(code, reason) },
            { t, response -> binanceCombineStreamListener.onFailure(t, response) }
        )
    }
}