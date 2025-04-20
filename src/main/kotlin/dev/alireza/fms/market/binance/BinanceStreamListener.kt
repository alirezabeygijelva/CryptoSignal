package dev.alireza.fms.market.binance

import com.binance.connector.client.utils.websocketcallback.WebSocketClosedCallback
import com.binance.connector.client.utils.websocketcallback.WebSocketClosingCallback
import com.binance.connector.client.utils.websocketcallback.WebSocketFailureCallback
import com.binance.connector.client.utils.websocketcallback.WebSocketMessageCallback
import com.binance.connector.client.utils.websocketcallback.WebSocketOpenCallback

interface BinanceStreamListener :
    WebSocketOpenCallback,
    WebSocketMessageCallback,
    WebSocketClosingCallback,
    WebSocketClosedCallback,
    WebSocketFailureCallback {
}