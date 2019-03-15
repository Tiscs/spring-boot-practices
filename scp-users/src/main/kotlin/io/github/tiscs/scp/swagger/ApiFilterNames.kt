package io.github.tiscs.scp.swagger

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilterNames(vararg val value: String, val required: Boolean = false)