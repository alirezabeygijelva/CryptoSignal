package dev.alireza.fms.config

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import java.time.Duration

@Configuration
@EnableCaching
class CachingConfig(
    private val properties: ConfigProperties
) {

    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer {
        return RedisCacheManagerBuilderCustomizer { builder ->
            builder
                .withCacheConfiguration(
                    "assets", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(properties.market.general.cacheTtl))
                        .disableCachingNullValues()
                )
                .withCacheConfiguration(
                    "binance", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(properties.market.general.cacheTtl))
                        .disableCachingNullValues()
                )
        }
    }
}