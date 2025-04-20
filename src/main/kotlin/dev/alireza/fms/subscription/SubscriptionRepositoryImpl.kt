package dev.alireza.fms.subscription

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.exception.ResponseException
import dev.alireza.fms.common.helper.PageRepositoryHelper
import dev.alireza.fms.jooq.tables.references.SUBSCRIPTIONS
import dev.alireza.fms.notification.NotificationType
import org.jooq.DSLContext
import org.jooq.Record3
import org.jooq.SortField
import org.jooq.impl.DSL
import org.jooq.kotlin.get
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import org.jooq.impl.DSL.count

@Repository
class SubscriptionRepositoryImpl : SubscriptionRepository {

    @Autowired
    private lateinit var ctx: DSLContext

    override fun findAllNotificationEnabled(marketId: Int, symbol: String): List<Subscription> {
        return ctx
            .selectFrom(SUBSCRIPTIONS)
            .where(SUBSCRIPTIONS.MARKET_ID.equal(marketId))
            .and(SUBSCRIPTIONS.SYMBOL.equal(symbol))
            .and(SUBSCRIPTIONS.NOTIFICATION_ENABLED.equal(1))
            .fetch()
            .map {
                Subscription(
                    it.id!!,
                    it.userId!!,
                    it.marketId!!,
                    it.symbol!!,
                    TargetType.fromString(it.targetType!!)!!,
                    it.targetValue!!,
                    it.message!!,
                    NotificationType.fromInt(it.notificationType!!),
                    it.notificationEnabled!! == 1.toByte(),
                    it.timeout!!,
                    it.lastTriggered?.toInstant(ZoneOffset.UTC),
                    it.createdAt!!.toInstant(ZoneOffset.UTC),
                    it.updatedAt!!.toInstant(ZoneOffset.UTC)
                )
            }
            .filter { subscription ->
                subscription.lastTriggered == null ||
                        subscription.lastTriggered.isBefore(Instant.now().minusSeconds(subscription.timeout))
            }
    }

    override fun updateLastTriggeredToNow(subscriptionId: Long) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.update(SUBSCRIPTIONS)
                    .set(SUBSCRIPTIONS.LAST_TRIGGERED, LocalDateTime.now(ZoneOffset.UTC))
                    .where(SUBSCRIPTIONS.ID.equal(subscriptionId))
                    .execute()
            }
        }
    }

    override fun updateAllLastTriggeredToNow(subscriptionIds: List<Long>) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.update(SUBSCRIPTIONS)
                    .set(SUBSCRIPTIONS.LAST_TRIGGERED, LocalDateTime.now(ZoneOffset.UTC))
                    .where(SUBSCRIPTIONS.ID.`in`(subscriptionIds))
                    .execute()
            }
        }
    }

    override fun findByUserId(userId: Long): List<Subscription> {
        return ctx
            .selectFrom(SUBSCRIPTIONS)
            .where(SUBSCRIPTIONS.USER_ID.equal(userId))
            .fetch()
            .map {
                Subscription(
                    it.id!!,
                    it.userId!!,
                    it.marketId!!,
                    it.symbol!!,
                    TargetType.fromString(it.targetType!!)!!,
                    it.targetValue!!,
                    it.message!!,
                    NotificationType.fromInt(it.notificationType!!),
                    it.notificationEnabled!! == 1.toByte(),
                    it.timeout!!,
                    it.lastTriggered?.toInstant(ZoneOffset.UTC),
                    it.createdAt!!.toInstant(ZoneOffset.UTC),
                    it.updatedAt!!.toInstant(ZoneOffset.UTC)
                )
            }
    }

    override fun findByUserId(userId: Long, pageable: Pageable): Page<Subscription> {
        return PageRepositoryHelper.findAll(ctx, SUBSCRIPTIONS, pageable, SUBSCRIPTIONS.USER_ID.equal(userId))
            .map {
                Subscription(
                    it.id!!,
                    it.userId!!,
                    it.marketId!!,
                    it.symbol!!,
                    TargetType.fromString(it.targetType!!)!!,
                    it.targetValue!!,
                    it.message!!,
                    NotificationType.fromInt(it.notificationType!!),
                    it.notificationEnabled!! == 1.toByte(),
                    it.timeout!!,
                    it.lastTriggered?.toInstant(ZoneOffset.UTC),
                    it.createdAt!!.toInstant(ZoneOffset.UTC),
                    it.updatedAt!!.toInstant(ZoneOffset.UTC)
                )
            }
    }

    override fun findByUserIdAndSymbol(userId: Long, symbol: String, pageable: Pageable): Page<Subscription> {
        return PageRepositoryHelper.findAll(
            ctx, SUBSCRIPTIONS, pageable, SUBSCRIPTIONS.USER_ID.equal(userId).and(
                SUBSCRIPTIONS.SYMBOL.equal(symbol)
            )
        )
            .map {
                Subscription(
                    it.id!!,
                    it.userId!!,
                    it.marketId!!,
                    it.symbol!!,
                    TargetType.fromString(it.targetType!!)!!,
                    it.targetValue!!,
                    it.message!!,
                    NotificationType.fromInt(it.notificationType!!),
                    it.notificationEnabled!! == 1.toByte(),
                    it.timeout!!,
                    it.lastTriggered?.toInstant(ZoneOffset.UTC),
                    it.createdAt!!.toInstant(ZoneOffset.UTC),
                    it.updatedAt!!.toInstant(ZoneOffset.UTC)
                )
            }
    }

    override fun findByUserIdGroupedBySymbol(userId: Long, pageable: Pageable): Page<Record3<Int?, String?, Int>> {
        var orderFileds = emptyArray<SortField<out Any>?>()
        if (pageable.sort.isSorted) {
            orderFileds = pageable.sort
                .map { order ->
                    if (order.isAscending) SUBSCRIPTIONS.get(order.property)
                        ?.asc() else SUBSCRIPTIONS.get(order.property)?.desc()
                }
                .toList()
                .toTypedArray()
        }
        try {
            val content = ctx.select(
                SUBSCRIPTIONS.MARKET_ID,
                SUBSCRIPTIONS.SYMBOL,
                count()
            )
                .from(SUBSCRIPTIONS)
                .where(SUBSCRIPTIONS.USER_ID.equal(userId))
                .groupBy(SUBSCRIPTIONS.SYMBOL, SUBSCRIPTIONS.MARKET_ID)
                .orderBy(*orderFileds)
                .limit(pageable.pageNumber, pageable.pageSize)
                .fetch()
                .toList()

            return PageImpl(
                content,
                pageable,
                ctx.fetchCount(
                    ctx.selectCount().from(SUBSCRIPTIONS).where(SUBSCRIPTIONS.USER_ID.equal(userId))
                        .groupBy(SUBSCRIPTIONS.SYMBOL, SUBSCRIPTIONS.MARKET_ID)
                ).toLong()
            )
        } catch (e: IllegalArgumentException) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.SORT_FIELD_NOT_SUPPORTED
            )
        }
    }

    override fun findByIdAndUserId(subscriptionId: Long, userId: Long): Subscription? {
        return ctx
            .selectFrom(SUBSCRIPTIONS)
            .where(SUBSCRIPTIONS.ID.equal(subscriptionId))
            .and(SUBSCRIPTIONS.USER_ID.equal(userId))
            .fetchOne()
            ?.run {
                Subscription(
                    this.id!!,
                    this.userId!!,
                    this.marketId!!,
                    this.symbol!!,
                    TargetType.fromString(this.targetType!!)!!,
                    this.targetValue!!,
                    this.message!!,
                    NotificationType.fromInt(this.notificationType!!),
                    this.notificationEnabled!! == 1.toByte(),
                    this.timeout!!,
                    this.lastTriggered?.toInstant(ZoneOffset.UTC),
                    this.createdAt!!.toInstant(ZoneOffset.UTC),
                    this.updatedAt!!.toInstant(ZoneOffset.UTC)
                )
            }
    }

    override fun insertSubscription(
        userId: Long,
        marketId: Int,
        symbol: String,
        targetType: TargetType,
        targetValue: BigDecimal,
        message: String,
        notificationType: NotificationType,
        notificationEnabled: Boolean,
        timeout: Long
    ) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.insertInto(SUBSCRIPTIONS)
                    .set(SUBSCRIPTIONS.USER_ID, userId)
                    .set(SUBSCRIPTIONS.MARKET_ID, marketId)
                    .set(SUBSCRIPTIONS.SYMBOL, symbol)
                    .set(SUBSCRIPTIONS.TARGET_TYPE, targetType.type)
                    .set(SUBSCRIPTIONS.TARGET_VALUE, targetValue)
                    .set(SUBSCRIPTIONS.MESSAGE, message)
                    .set(SUBSCRIPTIONS.NOTIFICATION_TYPE, notificationType.value)
                    .set(SUBSCRIPTIONS.NOTIFICATION_ENABLED, if (notificationEnabled) 1.toByte() else 0.toByte())
                    .set(SUBSCRIPTIONS.TIMEOUT, timeout)
                    .execute()
            }
        }
    }

    override fun updateSubscription(
        id: Long,
        userId: Long,
        targetValue: BigDecimal,
        message: String,
        notificationType: NotificationType,
        notificationEnabled: Boolean,
        timeout: Long
    ) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.update(SUBSCRIPTIONS)
                    .set(SUBSCRIPTIONS.TARGET_VALUE, targetValue)
                    .set(SUBSCRIPTIONS.MESSAGE, message)
                    .set(SUBSCRIPTIONS.NOTIFICATION_TYPE, notificationType.value)
                    .set(SUBSCRIPTIONS.NOTIFICATION_ENABLED, if (notificationEnabled) 1.toByte() else 0.toByte())
                    .set(SUBSCRIPTIONS.TIMEOUT, timeout)
                    .where(SUBSCRIPTIONS.ID.equal(id))
                    .and(SUBSCRIPTIONS.USER_ID.equal(userId))
                    .execute()
            }
        }
    }

    override fun deleteByIdAndUserId(id: Long, userId: Long) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.deleteFrom(SUBSCRIPTIONS)
                    .where(SUBSCRIPTIONS.ID.equal(id))
                    .and(SUBSCRIPTIONS.USER_ID.equal(userId))
                    .execute()
            }
        }
    }

    override fun isExistByIdAndUserId(subscriptionId: Long, userId: Long): Boolean {
        return ctx
            .selectFrom(SUBSCRIPTIONS)
            .where(SUBSCRIPTIONS.ID.equal(subscriptionId))
            .and(SUBSCRIPTIONS.USER_ID.equal(userId))
            .fetch()
            .isNotEmpty
    }

    override fun countByUserIdAndMarketIdAndSymbolAndTargetType(
        userId: Long,
        marketId: Int,
        symbol: String,
        targetType: TargetType
    ): Int {
        return ctx.fetchCount(
            DSL.selectFrom(SUBSCRIPTIONS)
                .where(SUBSCRIPTIONS.USER_ID.equal(userId))
                .and(SUBSCRIPTIONS.MARKET_ID.equal(marketId))
                .and(SUBSCRIPTIONS.SYMBOL.equal(symbol))
                .and(SUBSCRIPTIONS.TARGET_TYPE.equal(targetType.type))
        )
    }
}