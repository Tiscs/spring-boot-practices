package io.github.tiscs.sbp.models

data class Filter(val name: String, val params: List<Any?> = emptyList()) {
    fun mapParams(vararg keys: String): Map<String, Any?> =
        keys.indices.associate { keys[it] to if (params.size > it) this.params[it] else null }
}

data class Paging(val page: Int, val size: Int)

data class Query(
    val paging: Paging,
    val filter: Filter? = null,
    val orderBy: String? = null,
    val countOnly: Boolean = false,
)
