package dev.alireza.fms.market

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable
import java.time.Instant
import java.util.concurrent.TimeUnit

@RedisHash("assets")
data class Asset(
    @Id val symbol: String,
    val marketName: String,
    val openPrice: Double = 0.0,
    val closePrice: Double = 0.0,
    val highPrice: Double = 0.0,
    val lowPrice: Double = 0.0,
    val volume: Double = 0.0,
    val openTime: Long = 0,
    val closeTime: Long = 0,
    val updatedAt: Long = Instant.now().toEpochMilli(),
    @TimeToLive(unit = TimeUnit.SECONDS) val expirationInSeconds: Long
) : Serializable