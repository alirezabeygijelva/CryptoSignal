package dev.alireza.fms.dashboard.user

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.common.exception.ResponseException
import dev.alireza.fms.market.MarketRepository
import dev.alireza.fms.membership.User
import dev.alireza.fms.notification.NotificationType
import dev.alireza.fms.openapi.api.SubscriptionApi
import dev.alireza.fms.openapi.model.NewSubscriptionRequestBody
import dev.alireza.fms.openapi.model.SubscriptionDTO
import dev.alireza.fms.openapi.model.SubscriptionOverviewDTO
import dev.alireza.fms.openapi.model.SubscriptionRequestBody
import dev.alireza.fms.subscription.SubscriptionRepository
import dev.alireza.fms.subscription.TargetType
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_USER')")
class SubscriptionController(
    private val subscriptionRepository: SubscriptionRepository,
    private val marketRepository: MarketRepository
) : SubscriptionApi {

    override fun createSubscription(newSubscriptionRequestBody: NewSubscriptionRequestBody): ResponseApi<MutableList<SubscriptionDTO>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val targetType = TargetType.fromString(newSubscriptionRequestBody.targetType.value)
            ?: throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.INVALID_TARGET_TYPE
            )
        val sameSubscriptionCount = subscriptionRepository.countByUserIdAndMarketIdAndSymbolAndTargetType(
            userId = user.id,
            marketId = newSubscriptionRequestBody.marketId,
            symbol = newSubscriptionRequestBody.symbol,
            targetType = targetType
        )
        if (sameSubscriptionCount == 0) {
            if (marketRepository.isExistById(newSubscriptionRequestBody.marketId)) {
                subscriptionRepository.insertSubscription(
                    userId = user.id,
                    marketId = newSubscriptionRequestBody.marketId,
                    symbol = newSubscriptionRequestBody.symbol,
                    targetType = targetType,
                    targetValue = newSubscriptionRequestBody.targetValue,
                    message = newSubscriptionRequestBody.message,
                    notificationType = NotificationType.valueOf(newSubscriptionRequestBody.notificationType.value),
                    notificationEnabled = newSubscriptionRequestBody.notificationEnabled,
                    timeout = newSubscriptionRequestBody.timeout
                )
                return generateResponse(user.id)
            }
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.MARKET_NOT_FOUND
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.SUBSCRIPTION_EXISTS
        )
    }

    override fun deleteSubscription(subscriptionId: Long): ResponseApi<MutableList<SubscriptionDTO>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        if (subscriptionRepository.isExistByIdAndUserId(subscriptionId, user.id)) {
            subscriptionRepository.deleteByIdAndUserId(subscriptionId, user.id)
            return generateResponse(user.id)
        }
        throw ResponseException(
            HttpStatus.NOT_FOUND,
            ApiErrorConstant.SUBSCRIPTION_NOT_FOUND
        )
    }

    override fun getSubscription(subscriptionId: Long): ResponseApi<SubscriptionDTO> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, user.id)
        if (subscription != null) {
            val dto = SubscriptionDTO()
            BeanUtils.copyProperties(subscription, dto)
            dto.targetType = subscription.targetType.type
            dto.notificationType = subscription.notificationType.name
            dto.lastTriggered(
                if (subscription.lastTriggered != null) OffsetDateTime.ofInstant(
                    subscription.lastTriggered,
                    ZoneOffset.UTC
                ) else null
            )
            dto.updatedAt(OffsetDateTime.ofInstant(subscription.updatedAt, ZoneOffset.UTC))
            return ResponseApi(
                data = dto,
                message = "success"
            )
        }
        throw ResponseException(
            HttpStatus.NOT_FOUND,
            ApiErrorConstant.SUBSCRIPTION_NOT_FOUND
        )
    }

    override fun getSubscriptions(pageable: Pageable): ResponseApi<MutableList<SubscriptionDTO>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return generateResponse(user.id, pageable)
    }

    override fun getSubscriptionsBySymbol(
        symbol: String,
        pageable: Pageable
    ): ResponseApi<MutableList<SubscriptionDTO>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return generateResponse(user.id, symbol, pageable)
    }

    override fun getSubscriptionsGroupedBySymbol(pageable: Pageable): ResponseApi<MutableList<SubscriptionOverviewDTO>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val page = subscriptionRepository.findByUserIdGroupedBySymbol(user.id, pageable)
        return ResponseApi(
            data = page.content.map { record3 ->
                SubscriptionOverviewDTO()
                    .marketId(record3.get(0) as Int)
                    .symbol(record3.get(1) as String)
                    .signals(record3.get(2) as Int)
            }.toMutableList(),
            message = "success",
            page = page
        )
    }

    override fun updateSubscription(
        subscriptionId: Long,
        subscriptionRequestBody: SubscriptionRequestBody
    ): ResponseApi<MutableList<SubscriptionDTO>> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        if (subscriptionRepository.isExistByIdAndUserId(subscriptionId, user.id)) {
            subscriptionRepository.updateSubscription(
                id = subscriptionId,
                userId = user.id,
                targetValue = subscriptionRequestBody.targetValue,
                message = subscriptionRequestBody.message,
                notificationType = NotificationType.valueOf(subscriptionRequestBody.notificationType.value),
                notificationEnabled = subscriptionRequestBody.notificationEnabled,
                timeout = subscriptionRequestBody.timeout
            )
            return generateResponse(user.id)
        }
        throw ResponseException(
            HttpStatus.NOT_FOUND,
            ApiErrorConstant.SUBSCRIPTION_NOT_FOUND
        )
    }

    // -----------------------------
    // Private Helper
    // -----------------------------

    private fun generateResponse(userId: Long): ResponseApi<MutableList<SubscriptionDTO>> {
        return ResponseApi(
            data = subscriptionRepository.findByUserId(userId).map { subscription ->
                SubscriptionDTO()
                    .id(subscription.id)
                    .marketId(subscription.marketId)
                    .symbol(subscription.symbol)
                    .targetType(subscription.targetType.type)
                    .targetValue(subscription.targetValue)
                    .message(subscription.message)
                    .notificationType(subscription.notificationType.name)
                    .notificationEnabled(subscription.notificationEnabled)
                    .timeout(subscription.timeout)
                    .lastTriggered(
                        if (subscription.lastTriggered != null) OffsetDateTime.ofInstant(
                            subscription.lastTriggered,
                            ZoneOffset.UTC
                        ) else null
                    )
                    .updatedAt(OffsetDateTime.ofInstant(subscription.updatedAt, ZoneOffset.UTC))
            }.toMutableList(),
            message = "success"
        )
    }

    private fun generateResponse(userId: Long, pageable: Pageable): ResponseApi<MutableList<SubscriptionDTO>> {
        val page = subscriptionRepository.findByUserId(userId, pageable)
        return ResponseApi(
            data = page.map { subscription ->
                SubscriptionDTO()
                    .id(subscription.id)
                    .marketId(subscription.marketId)
                    .symbol(subscription.symbol)
                    .targetType(subscription.targetType.type)
                    .targetValue(subscription.targetValue)
                    .message(subscription.message)
                    .notificationType(subscription.notificationType.name)
                    .notificationEnabled(subscription.notificationEnabled)
                    .timeout(subscription.timeout)
                    .lastTriggered(
                        if (subscription.lastTriggered != null) OffsetDateTime.ofInstant(
                            subscription.lastTriggered,
                            ZoneOffset.UTC
                        ) else null
                    )
                    .updatedAt(OffsetDateTime.ofInstant(subscription.updatedAt, ZoneOffset.UTC))
            }.toMutableList(),
            message = "success",
            page = page
        )
    }

    private fun generateResponse(
        userId: Long,
        symbol: String,
        pageable: Pageable
    ): ResponseApi<MutableList<SubscriptionDTO>> {
        val page = subscriptionRepository.findByUserIdAndSymbol(userId, symbol, pageable)
        return ResponseApi(
            data = page.map { subscription ->
                SubscriptionDTO()
                    .id(subscription.id)
                    .marketId(subscription.marketId)
                    .symbol(subscription.symbol)
                    .targetType(subscription.targetType.type)
                    .targetValue(subscription.targetValue)
                    .message(subscription.message)
                    .notificationType(subscription.notificationType.name)
                    .notificationEnabled(subscription.notificationEnabled)
                    .timeout(subscription.timeout)
                    .lastTriggered(
                        if (subscription.lastTriggered != null) OffsetDateTime.ofInstant(
                            subscription.lastTriggered,
                            ZoneOffset.UTC
                        ) else null
                    )
                    .updatedAt(OffsetDateTime.ofInstant(subscription.updatedAt, ZoneOffset.UTC))
            }.toMutableList(),
            message = "success",
            page = page
        )
    }
}