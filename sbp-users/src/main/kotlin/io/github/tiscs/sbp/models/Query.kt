package io.github.tiscs.sbp.models

data class Filter(val name: String, val params: List<Any?> = emptyList()) {
    fun mapParams(vararg keys: String): Map<String, Any?> =
        keys.indices.associate { keys[it] to if (params.size > it) this.params[it] else null }
}

data class Sorting(val keys: List<String>, val modes: List<Mode>) {
    enum class Mode(val value: String) {
        ASC("ASC"),
        DESC("DESC"),
    }

    fun toMap(): Map<String, Mode> =
        keys.indices.associate { keys[it] to if (modes.size > it) this.modes[it] else Mode.ASC }
}

data class Paging(val page: Int, val size: Int)

data class Query(
    val paging: Paging,
    val filter: Filter? = null,
    val sorting: Sorting? = null,
    val countOnly: Boolean = false,
)
