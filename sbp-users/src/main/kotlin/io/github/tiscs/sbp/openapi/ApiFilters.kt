package io.github.tiscs.sbp.openapi

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilters(vararg val value: ApiFilter, val required: Boolean = false)
