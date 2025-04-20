package dev.alireza.fms.membership

data class Role(
    var id: Long,
    var name: String,
    var authorities: List<String>
)
