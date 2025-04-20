package dev.alireza.fms.config

import dev.alireza.fms.market.Asset
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
class RedisConfig {

    @Bean
    fun assetRedisTemplate(
        lettuceConnectionFactory: LettuceConnectionFactory
    ): RedisTemplate<String, Asset> {
        val template: RedisTemplate<String, Asset> = RedisTemplate()
        template.connectionFactory = lettuceConnectionFactory
        template.keySerializer = StringRedisSerializer.UTF_8
        template.valueSerializer = Jackson2JsonRedisSerializer(Asset::class.java)
        template.afterPropertiesSet()
        return template
    }

    @Bean
    fun redisTemplate(
        lettuceConnectionFactory: LettuceConnectionFactory
    ): RedisTemplate<String, Any> {
        val template: RedisTemplate<String, Any> = RedisTemplate()
        template.connectionFactory = lettuceConnectionFactory
        template.afterPropertiesSet()
        return template
    }
}