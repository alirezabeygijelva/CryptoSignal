package dev.alireza.fms.subscription

import dev.alireza.fms.membership.UserRepository
import dev.alireza.fms.notification.Notification
import dev.alireza.fms.notification.NotificationSendAllEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class SubscriptionListener(
    private val subscriptionRepository: SubscriptionRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val userRepository: UserRepository
) {

    @Async
    @EventListener(SubscriptionSignalEvent::class)
    fun signalEventHandler(event: SubscriptionSignalEvent) {
        if (event.marketId == null) return
        val triggeredSubscription = subscriptionRepository
            .findAllNotificationEnabled(event.marketId, event.asset.symbol)
            .filter { subscription ->
                subscription.targetType.isTargetFired(
                    event.asset,
                    subscription.targetValue.toDouble()
                )
            }

        val notifications = triggeredSubscription
            .map { subscription ->
                val user = userRepository.findById(subscription.userId).get()
                Notification(
                    user.clientId,
                    subscription.notificationType,
                    user.email,
                    user.phone,
                    user.telegramId,
                    String.format(
                        subscription.message,
                        subscription.targetType.valueCalculator(event.asset),
                        formatNumber(subscription.targetValue, 2)
                    ),
                    "${subscription.symbol} ${subscription.targetType.notificationSubject}",
                    null
                )
            }

        eventPublisher.publishEvent(NotificationSendAllEvent(notifications, this))
        subscriptionRepository
            .updateAllLastTriggeredToNow(triggeredSubscription.map { subscription -> subscription.id })
    }

    // ----------------------------------
    // Private Helper
    // ----------------------------------

    /**
     * Trims or rounds the decimal places of a number based on its current precision.
     *
     * @param number The number to be formatted.
     * @param maxDecimals The maximum number of decimal places to keep.
     * @return A string representation of the number with the specified decimal places.
     */
    fun formatNumber(number: BigDecimal, maxDecimals: Int): BigDecimal {
        val scale = number.scale()
        return if (scale > maxDecimals) {
            number.setScale(maxDecimals, RoundingMode.HALF_UP)
        } else {
            number
        }
    }
}