package dev.alireza.fms.market

import dev.alireza.fms.jooq.tables.records.MarketsRecord
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional

interface MarketRepository {

    fun findById(id: Int): Optional<MarketsRecord>

    fun findByName(name: String): Optional<MarketsRecord>

    fun findAll(): List<MarketsRecord>

    fun findAll(pageable: Pageable): Page<MarketsRecord>

    fun insertMarket(name: String, marketType: MarketType, description: String)

    fun deleteById(marketId: Int)

    fun isExistByName(name: String): Boolean

    fun isExistById(marketId: Int): Boolean
}