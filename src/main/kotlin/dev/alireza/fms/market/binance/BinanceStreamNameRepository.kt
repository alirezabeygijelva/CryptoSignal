package dev.alireza.fms.market.binance

interface BinanceStreamNameRepository {

    fun getAllStreamNames(): List<String>

    fun insertNewStream(streamName: String)

    fun deleteById(streamId: Int)

    fun deleteByName(streamName: String)

    fun isExistByName(streamName: String) : Boolean

    fun isExistById(streamId: Int) : Boolean
}