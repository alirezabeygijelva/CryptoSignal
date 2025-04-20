package dev.alireza.fms.market

import dev.alireza.fms.subscription.SubscriptionSignalEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class MarketAssetServiceImpl(
    private val assetRepository: AssetRepositry,
    private val eventPublisher: ApplicationEventPublisher,
    private val marketRepository: MarketRepository,
    private val simpMessagingTemplate: SimpMessagingTemplate
) : MarketAssetService {

    override fun save(asset: Asset) {
        // stores in the cache if it doesn't already exist
        if (assetRepository.findById(asset.symbol).isEmpty) {
            // publish singal event
            eventPublisher.publishEvent(
                SubscriptionSignalEvent(
                    marketRepository.findByName(asset.marketName).orElse(null).id,
                    asset,
                    this
                )
            )
            simpMessagingTemplate.convertAndSend("/topic/assets", asset)
            assetRepository.save(asset)
        }
    }
}