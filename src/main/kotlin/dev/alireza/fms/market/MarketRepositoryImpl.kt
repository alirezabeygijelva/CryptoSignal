package dev.alireza.fms.market

import dev.alireza.fms.common.helper.PageRepositoryHelper
import dev.alireza.fms.jooq.tables.records.MarketsRecord
import dev.alireza.fms.jooq.tables.references.MARKETS
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MarketRepositoryImpl : MarketRepository {

    @Autowired
    private lateinit var ctx: DSLContext

    override fun findById(id: Int): Optional<MarketsRecord> {
        val result = ctx.selectFrom(MARKETS)
            .where(MARKETS.ID.equal(id))
            .fetchOne()

        return Optional.ofNullable(result)
    }

    override fun findByName(name: String): Optional<MarketsRecord> {
        val result = ctx.selectFrom(MARKETS)
            .where(MARKETS.NAME.equal(name))
            .fetchOne()

        return Optional.ofNullable(result)
    }

    override fun findAll(): List<MarketsRecord> {
        return ctx.selectFrom(MARKETS)
            .fetch()
    }

    override fun findAll(pageable: Pageable): Page<MarketsRecord> {
        return PageRepositoryHelper.findAll(ctx, MARKETS, pageable)
    }

    override fun insertMarket(name: String, marketType: MarketType, description: String) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.insertInto(MARKETS)
                    .set(MARKETS.NAME, name)
                    .set(MARKETS.MARKET_TYPE, marketType.type)
                    .set(MARKETS.DESCRIPTION, description)
                    .execute()
            }
        }
    }

    override fun deleteById(marketId: Int) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.deleteFrom(MARKETS)
                    .where(MARKETS.ID.equal(marketId))
                    .execute()
            }
        }
    }

    override fun isExistByName(name: String): Boolean {
        return ctx.selectFrom(MARKETS)
            .where(MARKETS.NAME.equal(name))
            .fetch()
            .isNotEmpty
    }

    override fun isExistById(marketId: Int): Boolean {
        return ctx.selectFrom(MARKETS)
            .where(MARKETS.ID.equal(marketId))
            .fetch()
            .isNotEmpty
    }
}