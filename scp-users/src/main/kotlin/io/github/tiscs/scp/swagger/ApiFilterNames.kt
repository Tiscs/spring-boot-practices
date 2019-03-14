package io.github.tiscs.scp.swagger

import kotlin.annotation.Target
import kotlin.annotation.Retention

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilterNames(vararg val value: String, val required: Boolean = false)