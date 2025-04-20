package dev.alireza.fms.market.binance

import org.springframework.context.ApplicationEvent

class BinanceRestartEvent(val timeout: Long, source: Any) : ApplicationEvent(source) {
}