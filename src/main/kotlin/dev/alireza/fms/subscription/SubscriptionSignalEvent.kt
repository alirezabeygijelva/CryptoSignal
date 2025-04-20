package dev.alireza.fms.subscription

import dev.alireza.fms.market.Asset
import org.springframework.context.ApplicationEvent

class SubscriptionSignalEvent(
    val marketId: Int?,
    val asset: Asset,
    source: Any
) : ApplicationEvent(source) {
}