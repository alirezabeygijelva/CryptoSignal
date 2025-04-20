package dev.alireza.fms.subscription

import dev.alireza.fms.market.Asset

enum class TargetType(
    val type: String,
    val valueCalculator: (Asset) -> Double,
    val condition: (Double, Double) -> Boolean,
    val notificationSubject: String
) {

    MAX_PRICE(
        "MaxPrice",
        { asset -> asset.closePrice },
        { value, target -> value > target },
        "Goes Up ⬆️"
    ),
    MIN_PRICE(
        "MinPrice",
        { asset -> asset.closePrice },
        { value, target -> value < target },
        "Goes Down ⬇️"
    ),
    PRICE(
        "Price",
        { asset -> asset.closePrice },
        { value, target -> value == target },
        "Hits Target Price 🎯"
    ),
    VOLUME(
        "Volume",
        { asset -> asset.volume },
        { value, target -> value >= target },
        "Volume Alert 💹"
    ),
    MAX_CHANGE(
        "MaxChange",
        { asset -> ((asset.closePrice - asset.openPrice) / (asset.openPrice)) * 100.0 },
        { value, target -> value > target },
        "Big Gain 📈"
    ),
    MIN_CHANGE(
        "MinChange",
        { asset -> ((asset.closePrice - asset.openPrice) / (asset.openPrice)) * 100.0 },
        { value, target -> value < target },
        "Big Drop 📉"
    ),
    CHANGE(
        "Change",
        { asset -> ((asset.closePrice - asset.openPrice) / (asset.openPrice)) * 100.0 },
        { value, target -> value == target },
        "Hit Target Change 🎯"
    );

    companion object {
        fun fromString(type: String): TargetType? {
            return entries.find { it.type.equals(type, ignoreCase = true) }
        }
    }

    fun isTargetFired(asset: Asset, target: Double): Boolean {
        val value = this.valueCalculator(asset)
        return this.condition(value, target)
    }
}
