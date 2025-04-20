package dev.alireza.fms.market.binance

import dev.alireza.fms.jooq.tables.references.BINANCE_STREAMS
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BinanceStreamNameRepositoryImpl : BinanceStreamNameRepository {

    @Autowired
    private lateinit var ctx: DSLContext

    override fun getAllStreamNames(): List<String> {
        val result = ctx.select()
            .from(BINANCE_STREAMS)
            .fetch(BINANCE_STREAMS.STREAM_NAME)
            .filterNotNull()
        return result
    }

    override fun insertNewStream(streamName: String) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.insertInto(BINANCE_STREAMS)
                    .set(BINANCE_STREAMS.STREAM_NAME, streamName)
                    .execute()
            }
        }
    }

    override fun deleteById(streamId: Int) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.deleteFrom(BINANCE_STREAMS)
                    .where(BINANCE_STREAMS.ID.equal(streamId))
                    .execute()
            }
        }
    }

    override fun deleteByName(streamName: String) {
        ctx.transaction { configuration ->
            run {
                val dslContext: DSLContext = DSL.using(configuration)
                dslContext.deleteFrom(BINANCE_STREAMS)
                    .where(BINANCE_STREAMS.STREAM_NAME.equal(streamName))
                    .execute()
            }
        }
    }

    override fun isExistByName(streamName: String): Boolean {
        return ctx.selectFrom(BINANCE_STREAMS)
            .where(BINANCE_STREAMS.STREAM_NAME.equal(streamName))
            .fetch()
            .isNotEmpty
    }

    override fun isExistById(streamId: Int): Boolean {
        return ctx.selectFrom(BINANCE_STREAMS)
            .where(BINANCE_STREAMS.ID.equal(streamId))
            .fetch()
            .isNotEmpty
    }
}