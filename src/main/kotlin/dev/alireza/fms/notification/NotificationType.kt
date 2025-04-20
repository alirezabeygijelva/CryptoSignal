package dev.alireza.fms.notification

enum class NotificationType(val value: Int) {
    UNKNOWN(-1),
    EMAIL(1),
    SMS(2),
    TELEGRAM(3);

    companion object {
        fun fromInt(value: Int): NotificationType {
            return entries.find { it.value == value } ?: UNKNOWN
        }
    }
}