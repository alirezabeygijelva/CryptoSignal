package dev.alireza.fms.subscription

import dev.alireza.fms.notification.NotificationType
import java.math.BigDecimal
import java.time.Instant

data class Subscription(
    val id: Long,
    val userId: Long,
    val marketId: Int,
    val symbol: String,
    val targetType: TargetType,
    val targetValue: BigDecimal,
    val message: String,
    val notificationType: NotificationType,
    val notificationEnabled: Boolean,
    val timeout: Long,
    val lastTriggered: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant
)
