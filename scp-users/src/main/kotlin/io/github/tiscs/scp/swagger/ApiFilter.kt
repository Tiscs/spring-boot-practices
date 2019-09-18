package io.github.tiscs.scp.swagger

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilter(val name: String, val description: String = "")
