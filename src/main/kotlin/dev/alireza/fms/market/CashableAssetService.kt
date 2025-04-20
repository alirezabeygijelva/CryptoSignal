package dev.alireza.fms.market

import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable

interface CashableAssetService {

    @Cacheable(key = "#symbol", condition = "#root.caches.size() == 1")
    fun get(symbol: String): Asset

    @Cacheable(key = "#symbol", condition = "#root.caches.size() >= 1")
    fun getAny(symbol: String): Asset

    @CachePut(key = "#symbol", condition = "#root.caches.size() == 1")
    fun forceGet(symbol: String): Asset

    @CachePut(key = "#symbol", condition = "#root.caches.size() >= 1")
    fun forceGetAny(symbol: String): Asset
}