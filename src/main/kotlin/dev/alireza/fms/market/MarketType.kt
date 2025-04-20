package dev.alireza.fms.market

enum class MarketType(val type: String) {
    CRYPTOCURRENCY("Cryptocurrency"),
    FOREX("Forex"),
    STOCK("Stock"),
    COMMODITY("Commodity"),
    INDEX("Index"),
    OTHER("Other");

    companion object {
        fun fromString(type: String): MarketType? {
            return entries.find { it.type.equals(type, ignoreCase = true) }
        }
    }
}