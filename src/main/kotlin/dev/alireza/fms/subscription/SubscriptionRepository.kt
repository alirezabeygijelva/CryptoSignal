package dev.alireza.fms.subscription

import dev.alireza.fms.notification.NotificationType
import org.jooq.Record3
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.math.BigDecimal

interface SubscriptionRepository {

    fun findAllNotificationEnabled(marketId: Int, symbol: String): List<Subscription>

    fun updateLastTriggeredToNow(subscriptionId: Long)

    fun updateAllLastTriggeredToNow(subscriptionIds: List<Long>)

    fun findByUserId(userId: Long): List<Subscription>

    fun findByUserId(userId: Long, pageable: Pageable): Page<Subscription>

    fun findByUserIdAndSymbol(userId: Long, symbol: String, pageable: Pageable): Page<Subscription>

    fun findByUserIdGroupedBySymbol(userId: Long, pageable: Pageable): Page<Record3<Int?, String?, Int>>

    fun findByIdAndUserId(subscriptionId: Long, userId: Long): Subscription?

    fun insertSubscription(
        userId: Long,
        marketId: Int,
        symbol: String,
        targetType: TargetType,
        targetValue: BigDecimal,
        message: String,
        notificationType: NotificationType,
        notificationEnabled: Boolean,
        timeout: Long
    )

    fun updateSubscription(
        id: Long,
        userId: Long,
        targetValue: BigDecimal,
        message: String,
        notificationType: NotificationType,
        notificationEnabled: Boolean,
        timeout: Long
    )

    fun deleteByIdAndUserId(id: Long, userId: Long,)

    fun isExistByIdAndUserId(subscriptionId: Long, userId: Long): Boolean

    fun countByUserIdAndMarketIdAndSymbolAndTargetType(userId: Long, marketId: Int, symbol: String, targetType: TargetType): Int
}