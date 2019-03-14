package io.github.tiscs.scp.swagger

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilterNames(vararg val value: String, val required: Boolean = false)