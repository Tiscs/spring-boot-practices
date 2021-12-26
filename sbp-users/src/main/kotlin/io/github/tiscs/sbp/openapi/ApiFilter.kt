package io.github.tiscs.sbp.openapi

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilter(val name: String, val example: String = "", val description: String = "")
